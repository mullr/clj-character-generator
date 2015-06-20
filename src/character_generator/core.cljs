(ns ^:figwheel-always character-generator.core
  (:require [reagent.core :as r :refer [atom]]
            [character-generator.character :refer [the-fighter]]
            [character-generator.utils :refer [mapvals]]))

(enable-console-print!)

;;; Data formatting utils
(defn class-str [level-seq]
  (->> level-seq
       (map (fn [{:keys [class level]}]
              (str class " " level)))
       (clojure.string/join ", ")))

(defn stat-bonus [stat]
  (-> stat
      (- 10)
      (/ 2)
      Math/floor))

(defn format-bonus [bonus]
  (str (if (pos? bonus) "+" "")
       bonus))


;;; Layout utils
(defn quick-table [opts model & selector-seq]
  (fn []
   [:table.table opts
    (list
     (if (:title opts)
       [:tr [:th {:colspan 2} (:title opts)]])
     (for [[label selector] selector-seq]
       (let [key (str label selector)]
         [:tr
          [:td label]
          [:td (selector @model)]])))]))

(defn quick-dl [opts model & selector-seq]
  (fn []
    [:dl opts
     (for [[label selector] selector-seq]
       (let [key (str label selector)]
         (list [:dt label]
               [:dd (selector @model)])))]))


;;; Character sheet building blocks
(defn header-view [c]
  [:table.table
   [:tr
    [:td (-> @c :level class-str)]
    [:td (:background @c)]
    [:td (:player-name @c)]]
   [:tr
    [:td (:race @c)]
    [:td (:alignment @c)]
    [:td (:xp @c) " XP"]]])

(defn inspiration-block-view [c]
  (fn []
    [:div.panel.panel-default
     [quick-table {} c
      ["Inspiration" :inspiration]
      ["Proficiency Bonus" :proficiency-bonus]]]))

(defn saving-throws-view [saving-throws]
  (fn []
    [:div.panel.panel-default
     [quick-table {:title "Saving Throws"} saving-throws
      ["Strength"     (comp format-bonus :value :str)]
      ["Dexterity"    (comp format-bonus :value :dex)]
      ["Constitution" (comp format-bonus :value :con)]
      ["Intelligence" (comp format-bonus :value :int)]
      ["Wisdom"       (comp format-bonus :value :wis)]
      ["Charisma"     (comp format-bonus :value :cha)]]]))

(defn stats-view [c]
  (fn []
    [:div
     (for [stat [:str :dex :con :int :wis :cha]]
       (let [val (-> @c :stats stat)]
         [:div.panel.panel-default.stat-box
          [:div (.toUpperCase (name stat))]
          [:b (format-bonus (stat-bonus val))]
          [:div val]]))]))

(defn character-background-view [c]
  (fn []
    [:div.panel.panel-default
     [quick-table {} c
      ["Personality Traits" :personality]
      ["Ideals" :ideals]
      ["Bonds" :bonds]
      ["Flaws" :flaws]]]))

(defn combat-view [c]
  (fn []
    [:div.panel.panel-default 
     [quick-table {} c
      ["Armor Class" :ac]
      ["Initiative" :initiative]
      ["Speed" :speed]
      ["Hit Points" :hp]
      ["Hit Dice" #(first (:hit-dice %))]
      ["Death Saves" :death-saves]]]))

;;; Top level layout
(defn character-sheet-view [c]
  (fn []
    [:div

     [:div.row
      [:div.col-sm-4 [:h1 (:name @c)]]
      [:div.col-sm-8 [header-view c]]]

     [:div.row
      [:div.col-md-1.col-sm-2 [stats-view c]]
      [:div.col-md-3.col-sm-3
       [inspiration-block-view c]
       [saving-throws-view (r/cursor c [:saving-throws])]]
      [:div.col-md-4.col-sm-3
       [combat-view c]]
      [:div.col-md-4.col-sm-3
       [character-background-view c]]]]))


;;; App state and top-level component
(defonce app-state (atom {:character the-fighter}))

(r/render-component
 [character-sheet-view (r/cursor app-state [:character])]
 (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
