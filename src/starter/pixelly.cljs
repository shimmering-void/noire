;; code from http://www.quil.info/

(ns starter.pixelly
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))


(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 60)
  (q/no-smooth)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  (def dither (q/load-shader "dither.frag" "dither.vert"))
  (def gfx (q/create-graphics (q/width) (q/height) :p3d))

  {:color 0
   :angle 0
   :t 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)
   :t (inc (:t state))})

(defn draw-state [{:keys [color t]}]
  (q/fill 255 255 255)
  ;; (q/stroke (/ color 2) 255 255)
  ;; (q/stroke-weight 4)
  (q/background 0)
  (q/camera (+ 150 (* 100 (q/sin (/ t 50.0)))) (+ 150 (* 100 (q/cos (/ t 50.0)))) 150 0 0 0 0 0 -1)
  (q/specular 250)
  (q/directional-light 255 0 0 0.25 0.25 0)
  (q/point-light 0 0 255 0 0 100)
  (q/ambient-light 50 50 50)
  (doseq [i (range 0 3)]
    (q/with-translation [(* i 64) 0 0]
      (q/box 32))

    (q/with-translation [0 0 (* i 64)]
      (q/box 32))

    (q/with-translation [0 (* i 64) 0]
      (q/box 32)))
    ;; (q/shader dither)
  )

; this function is called in index.html
(defn ^:export run-sketch []
  (q/defsketch noire
    :host "app"
    :size [60 60]
    :renderer :p3d
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
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
