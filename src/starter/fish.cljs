;; code from http://www.quil.info/

(ns starter.quil
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [starter.util :as u]))

(defn terrain-height [t]
  (q/ceil (* t 10)))

(defn grid [w h]
  (map (fn [x] (map (fn [y] [x y]) (range 0 h))) (range 0 w)))

(defn grid-3d [w h d]
  (map (fn [x] (map (fn [y] (map (fn [z] [x y z]) (range 0 d))) (range 0 h))) (range 0 w)))

(def width 16)
(def height 16)
(def depth 16)

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
   :grid (grid-3d width height depth)})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)
   :t (inc (:t state))
   :grid (:grid state)})

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
  (let [t (/ t 10)]
    (q/fill 255 255 255)
  ;; (q/stroke (/ color 2) 255 255)
  ;; (q/stroke-weight 4)
    (q/background 0)
    (q/camera (+ 512 (* 256 (q/sin (/ t 2)))) (+ 512 (* 256 (q/cos (/ t 2)))) (+ 512 (* 256 (q/atan (/ t 2)))) 0 0 0 0 0 -1)
    ;; (q/camera 250 250 250 0 0 0 0 0 -1)
    (q/ortho -480 480 -400 1000 -1000 10000)
  ;; (q/specular 250)
    (q/directional-light 255 0 0 25 25 25)
    ;; (q/point-light 0 0 255 0 0 100)
    (q/ambient-light 50 100 50)
    ;; (q/sphere-detail 15)

    (q/with-translation [(- (/ (* width space) 2)) (- (/ (* height space) 2)) -128]
      (doseq [row grid]
        (doseq [col row]
          (doseq [[x y z] col]
            (let [noise-sample (uber-noise (+ x (/ t 100)) (+ y (/ t 100)) z)
                  size (* 0.7 size noise-sample)]
              (when (> noise-sample 0.5)
              ;; (println z)
                (let [n (q/cos (* 2 q/PI (q/sin (* 0.8 q/PI (uber-noise x  y  (* 0.01 z))))))]
                  (q/with-translation
                    [(+ (q/cos x) (* x  space (+ 0.9 (* 0.1 (q/sin t)))))
                     (+ (q/cos y) (*  y space (+ 0.9 (* 0.1 (q/cos t)))))
                     (+ (q/atan z) (+  (* space z)) (* 2 (q/sin (+ x y t))))]
                    (q/fill 0 0 0)
                    (q/stroke (+ 80 (* 30 noise-sample (/ z 10)) (* 20 (q/sin (/ t 10))) (* 20 (q/cos (/ t 2))) (* 30 (/ z 3.5) n)) (+ 160 (* 64 (q/sin (* n 2 q/PI)))) (+ 40 (* z 24)))
                    (q/stroke-weight 5)
                    (q/rotate (* 2 q/PI (/ t 10) n))
                    (q/ellipsoid (- size (* z 1)) (* 4 size (q/sin (* (/ z 5) t n))) (* size (q/cos  (* (/ z 5) t n))))))))))))))

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
