(ns character-generator.character)

(enable-console-print!)

;; goals:
;; - automatically calculate stat bonuses, saving throws, skill bonuses
;; - track expendable resources

(def the-fighter
  {:name "The Fighter"
   :level [{:class "Fighter"
            :level 1}]
   :race "Human"
   :background "Noble"
   :alignment "Lawful neutral"
   :player-name "Russell"
   :xp 0

   :personality (str "My flattery makes those I talk to feel wonderful and important. "
                     "Also, I don’t like to get dirty, and I won’t be caught dead in"
                     "unsuitable accommodations.")
   :ideals (str "Responsibility. It’s the duty of a noble to protect the common "
                "people, not bully them.")
   :bonds (str "My greataxe is a family heirloom, and it’s by far my most "
               "precious possession.")
   :flaws (str "I have a hard time resisting the allure of wealth, especially "
               "gold. Wealth can help me restore my legacy.")

   :stats {:str 16
           :dex 9
           :con 15
           :int 11
           :wis 13
           :cha 14}

   :saving-throws {:str {:proficient true}
                   :con {:proficient true}}
   :skills {:athletics {:proficient true}
            :history {:proficient true}
            :perception {:proficient true}
            :persuasion {:proficient true}
            :stealth {:modifiers [{:roll :disadvantage
                                   :reason "Chain mail"}]}}

   :proficiencies ["all armor" "shields" "simple weapons"
                   "martial weapons" "playing cards"]
   :languages ["Common" "Draconic" "Dwarvish"]

   :ac {:modifiers [{:bonus 1
                     :reason "Fighting Style (Defense)"}]}
   :initiative {}
   :speed {:base 30}
   :hp {:max 12}
   :hit-dice [{:sides 10, :quantity 1}]

   :equipment ["chain mail"
               {:name "greataxe"
                :type "weapon"
                :damage {:sides 12
                         :quantity 1
                         :type "slashing"}}
               {:name "javelin"
                :type "weapon"
                :damage {:sides 6
                         :quantity 1
                         :type "piercing"}
                :quantity 3
                :range [30 120]}
               "backpack" "blanket" "tinderbox"
               {:name "day of rations"
                :quantity 2}
               "waterskin"
               "set of fine clothes" "signet ring"
               "scroll of pedigree"]
   :money {:gp 25}

   :features
   [{:name "Second Wind"
     :description
     (str "You have a limited well of stamina you can draw on to protect "
          "yourself from harm. You can use a bonus action to regain hit points "
          "equal to 1d10 + your fighter level.\nOnce you use this feature, "
          "you must finish a short or long rest before you can use it again.")
     :uses 1
     :recharge #{:short-rest :long-rest}}
    {:name "Fighting Style (Defense)"
     :description
     (str "While you are wearing armor, you gain a +1 bonus to AC. This bonus "
          "is already included in your AC.")}

    {:name "Position of Privilege"
     :description
     "Thanks to your noble birth, people are inclined to think the best of you.
      You are welcome in high society, and people assume you have the right to
      be wherever you are. The common folk make every effort to accommodate you
      and avoid your displeasure, and other people of high birth treat you as a
      member of the same social sphere. You can secure an audience with a local
      noble if you need to."}]})
