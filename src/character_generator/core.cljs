(ns ^:figwheel-always character-generator.core
  (:require [reagent.core :as r :refer [atom]]
            [character-generator.character :refer [the-fighter]]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello world!"
                          :character the-fighter}))

;; show the most important info above the fold:
;; views: combat, exploration, rp, complete

(defn class-str [level-seq]
  (->> level-seq
       (map (fn [{:keys [class level]}]
              (str class " " level)))
       (clojure.string/join ", ")))

(defn stat-bonus [stat]
  (let [bonus (-> stat
                  (- 10)
                  (/ 2)
                  Math/floor)]
    (str (if (pos? bonus) "+" "")
         bonus))) 

(defn brief-stat [stat-value]
  (str stat-value " (" (stat-bonus stat-value) ")"))

(defn quick-dl [model & selector-seq]
  [:dl
   (for [[label selector] selector-seq]
     (list
      [:dt label] [:dd (selector model)]))])

(defn mapvals [m f]
  (->> m
       (map (fn [[k v]] [k (f v)]))
       (into {})))

(defn character-sheet [c]
  (fn []
    [:h1 (:name @c)]
    [:div
     (quick-dl @c
               ["Class" (comp class-str :level)]
               ["Race" :race]
               ["Background" :background]
               ["Alignment" :alignment]
               ["Player Name" :player-name]
               ["XP" :xp])
     (quick-dl @c
               ["Personality" :personality]
               ["Ideals" :ideals]
               ["Bonds" :bonds]
               ["Flaws" :flaws])
     (quick-dl (mapvals (:stats @c) brief-stat)
               ["STR" :str]
               ["DEX" :dex]
               ["CON" :con]
               ["INT" :int]
               ["WIS" :wis]
               ["CHA" :cha])]))

(r/render-component
 [character-sheet (r/cursor app-state [:character])]
 (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
