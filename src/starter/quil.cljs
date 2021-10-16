;; code from http://www.quil.info/

(ns starter.quil
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [starter.util :as u]))

(defn grid [w h]
  (map (fn [x] (map (fn [y] [x y]) (range 0 h))) (range 0 w)))

(grid 4 4)

(def width 128)
(def height 128)

(def palette [[64 48 73]
              [63 63 108]
              [134 92 92]
              [32 26 56]
              [172 152 216]])

;; (def palette [[255 165 0]
;;               [171 255 45]
;;               [0 172 245]
;;               [179 25 84]])

(def characters ["⇇" "⇲" "∱" "↭" "⋙"])
;; (def characters ["◐" "◓" "◕" "◉"])

(def ramp {0 (get palette 0)
           0.25 (get palette 1)
           0.5 (get palette 2)
           0.75 (get palette 3)})

(defn colour-from-ramp [val]
  (get palette (q/round (q/lerp 0 (dec (count palette)) val))))

(defn char-from-ramp [val]
  (get characters (q/round (q/lerp 0 (dec (count characters)) val))))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 2)
  (q/no-smooth)
  ;; (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:t 0
   :grid (grid width height)})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (merge state {:t (inc (:t state))}))

(def size 32)
(def space (+ size 3))

(defn uber-noise [x y z]
  (let [o1 0.5
        o2 5
        o3 15]
    (/ (+ (q/exp (q/noise (* o1 x) (* o1 y) (* o1 z)))
          (q/exp (q/noise (* o2 x) (* o2 y) (* o2 z)))
          (q/exp (q/noise (* o3 x) (* o3 y) (* o3 z)))) 10)))

(defn draw-state [{:keys [grid t]}]
  (let [t' (/ t 100)
        t'' (/ t 10)]
    (q/fill 255 255 255)
    (q/stroke-weight 0)
    (q/background 0)
    (q/text-font "Courier" 24)
    ;; (q/ellipse 32 32 32 32)
    (doseq [row grid]
      (doseq [[x y] row]
        (let [nx (- x (/ width 2))
              ny (- y (/ height 2))]
          (let [n (q/constrain (* (/ 2 (+ 1 (q/cos (* 1.31 t'')))) (q/noise (* 2 nx) (* 3 ny) t'') (q/noise nx ny t')) 0 1)]
            ;; (apply q/fill (colour-from-ramp n))
            ;; (q/rect (* 16 x) (* 16 y) 16 16)
            (when (> (q/exp n) 1.5)
              (apply q/fill (colour-from-ramp (- 1 n)))
              ;; (q/text-font "Courier" (* n 24))
              (q/text (char-from-ramp n) (- (* 16 x) 3) (+ (* 16 y) 14)))))))))

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch noire
    :host "app"
    :size [2048 2048]
    :renderer :p2d
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    :key-pressed (u/save-image "grid.png")
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
    :middleware [m/fun-mode]))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (js/console.log "start"))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (run-sketch)
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
