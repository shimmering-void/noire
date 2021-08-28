;; code from http://www.quil.info/

(ns starter.digital-city
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [starter.util :as u]))

(defn terrain-height [t]
  (q/ceil (* t 10)))

(defn grid [w h]
  (map (fn [x] (map (fn [y] [x y]) (range 0 h))) (range 0 w)))

(def width 32)
(def height 32)

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ;; (q/no-smooth)
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0
   :t 0
   :grid (grid width height)})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)
   :t (inc (:t state))
   :grid (:grid state)})

(def size 32)
(def space (+ size 3))

(defn draw-state [{:keys [grid t]}]
  (let [t (/ t 10)]
    (q/fill 255 255 255)
  ;; (q/stroke (/ color 2) 255 255)
  ;; (q/stroke-weight 4)
    (q/background 0)
    (q/camera (+ 512 (* 100 (q/sin (/ t 5)))) (+ 512 (* 100 (q/sin (/ t 5)))) 512 0 0 0 0 0 -1)
    ;; (q/camera 250 250 250 0 0 0 0 0 -1)
    (q/ortho -1024 1024 -1024 1024 -1000 10000)
  ;; (q/specular 250)
    (q/directional-light 255 0 0 25 25 25)
    ;; (q/point-light 0 0 255 0 0 100)
    (q/ambient-light 50 100 50)
    ;; (q/sphere-detail 15)

    (q/with-translation [(- (/ (* width space) 2)) (- (/ (* height space) 2)) -128]
      (doseq [row grid]
        (doseq [[x y] row]
          (let [n (q/cos (* 2 q/PI (q/sin (* 0.5 q/PI (q/noise (* x 0.2) (* y 0.2))))))]
            (doseq [z (range 0 (terrain-height n))]
              (q/with-translation
                [(+ (* x space))
                 (+ (* y space))
                 (+ (+ (* space z)) (* 2 (q/sin (+ x y t))))]
                (q/fill 0 0 0)
                (q/stroke (+ 65 (* 80 (/ z 4.5) n)) (+ 128 (* 64 (q/sin (* n 2 q/PI)))) (+ 20 (* z 32)))
                (q/stroke-weight 3)
                (q/rotate (* 2 q/PI (/ t 10) n))
                (q/box (- size (* z 1)) (* size (q/sin (* (/ z 5) t n))) (* size (q/cos  (* (/ z 5) t n))))))))))))

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch noire
    :host "app"
    :size [2048 2048]
    :renderer :p3d
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    :key-pressed (u/save-image "iso.png")
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
