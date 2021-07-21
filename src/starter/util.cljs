(ns starter.util
  (:require [quil.core :as q]))

(defn save-image
  [filename]
  (fn [state {:keys [key key-code]}]
    (case key
      (:s) (do
             (println "Saving file...")
             (q/save (str (.getTime (js/Date.)) filename)))
      state)))