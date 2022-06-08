(ns kid-shared.data.blocks
  (:require [kid-game.components.shared.icons      :as icons]))

(def web-search-explanation
  [:<>
   [:h3 "Sometimes a simple web search is all you need to verify a post:"]
   [:p "Bring up your favorite search engine and type in a couple of relevant keywords"]
   [:p "Extract keywords straight from the text (e.g. the name of an institution) or use them to describe content on a meta level (report on XYZ)."]
   [:p "To get better results, put multiword terms in quotes (\"European Union\"); don't forget to use Boolean operators (\"European Union\" AND \"climate report\")"]
   [:p "A search with the right keywords ideally leads you to an original source or a secondary piece of content that refers to it"]
   [:p "Compare your sources with what people claim on social media – and get a good hunch of what's legit and what's not"]])

(def image-analysis-explanation
  [:<>
   [:h3 "An image analysis tells you whether or not an image has been tampered with: "]
   [:p "For a basic analysis, all the tools you need are your eyes."]
   [:p "Take a closer look at the image:  Are there any weird looking spots? Is there anything off about the perspective? About the lighting? The colors? Do people look like they are out of place or hovering?"]
   [:p "If the answer to one or more of the above questions is \"yes\", you're probably looking at a manipulated image. "]
   [:p "If you're not sure, use a digital zoom (or a set of forensics tools) to investigate further."]])

(def ris-explanation
  [:<> [:h3 "An RIS tells you whether or not an image has been used on the (open) web before – and in which context:"]
   [:p "Run the image in question through an image search engine of your choice (e.g. the one by Megacorp)"]
   [:p "Does the image show up in lots of other places on the web (especially with different dates and descriptions)? "]
   [:p "If the answer is yes, it’s likely the image on BLEEPER is not an original – and people making bold claims about it are probably NOT telling the truth."]])

(def ris-crop-explanation
  [:<>
   [:h3 "Sometimes, an RIS doesn't yield any results. One reason could be that the image shows too many details. In that case:"]
   [:p "Try putting the focus on the part you really need to identify, like a specific building or person."]
   [:p "Repeat the search with the cropped image."]
   [:p "Do the available results allow you to identify the item you've selected?"]
   [:p "If the answer is no, any claim about the image should be treated with caution."]] )

(def ris-flip-explanation
  [:<>
   [:h3 "Sometimes, an RIS doesn't yield any results. One reason could be that the image has been inverted. In that case:"]
   [:p "Try re-inverting the image"]
   [:p "Repeat the search"]])

(def game-tagline-title "Welcome to Go Verify!")

(def game-tagline-welcome "You've heard about that whole \"fake news\" thing, haven't you?")

(def game-explanation
  [:<>
   [:p "We've created this little browser game to teach you the basics of verification. That's just a fancy term for thoroughly checking news items and social media messages that may contain false information – or worse."]
   [:p "Now as soon as you've entered your name and clicked on the \"login\" button, you'll be taken to \"Bleeper\". That's a fictitious social network in a not so distant future which might be more realistic than you like."]
   [:p "On the platform, you'll see a lot of posts sliding in and out of your timeline. In this demo, you can read all of them, and you’re free to interact with some of them -- hopefully after you've taken a closer look."]
   [:p "You'll get the hang of this soon. And if you don't, feel free to ask your friend Izzy: the duck!"]])

(def tutorial-slide-1
  [:div.columns.ml-0.mr-0
   [:div.column.is-half.p4
    [:img {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/tutorial-slide-1.jpg"}]]
   [:div.column.is-half.pl-4
    [:p.mb-4 "Welcome to Bleeper, Europe's Nr. 1 social media platform – and welcome to the year 2032!"]
    [:p "The EU has changed quite a bit, but it's still facing the same societal challenges. There's the climate crisis, economic injustice, nationalism, and weird people with even weirder ideas."]]
   ])

(def tutorial-slide-2
  [:div.columns.ml-0.mr-0
   [:div.column.is-half.p4
    [:img {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/tutorial-slide-2.jpg"}]]
   [:div.column.is-half.pl-4
    [:p.mb-4 "You're right in the middle of this: Trying to keep up with the news, trying to keep friends and family well-informed, trying to keep sane in a social network full of disinfo and hate speech."]
    ]])

(def tutorial-slide-3
  [:div.columns.ml-0.mr-0
   [:div.column.is-half.p4
    [icons/izzy-with-speech!?-bubble]]
   [:div.column.is-half.pl-4
    [:p.mb-4 "As you play along, a lot of posts will appear in your timeline.  You can read all of them, and you'll be able to interact with some of them."]
    [:p.mb-4 "Whenever Izzy – the duck – appears, you should take a closer look. Izzy will offer a choice of verification actions and also explain what they're about."]
    [:p.mb-4 "Once you've investigated a post, you can make a call: Share or block? Either decision can win or cost you points. "]
    [:p.mb-4 "Don't spread disinfo. Don't ignore legit content. Don't fail your friends. And don't wait too long, or else that Bleeper post may just go viral..."]
    ]])

(def credits
  [:div
   [:h3.mb-4 "Credits | Legal note"]
   [:p.mb-4 "This game was created in the scope of the " [:a {:href   "https://innovation.dw.com/"
                                                              :target "_blank"} "DW Innovation project"]]
   [:p.mb-4 [:b "K"] "ünstliche " [:b "I"] "ntelligenz gegen " [:b "D"] "esinformation (KID)."]

   [:p.mb-4 "Concept & Development: DW Innovation"]
   [:p.mb-4 "Graphic design: Andreas Leibe"]
   [:p.mb-4 "Funding: " [:a {:href   "https://www.bundesregierung.de/breg-de/bundesregierung/staatsministerin-fuer-kultur-und-medien"
                             :target "_blank"} "Bundesbeauftage für Kultur und Medien"]]
   [:p.mb-4 [:img {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/bkm_logo.png" :style {:max-width "500px"}}]]

   [:p.mb-4 "Legal Note: " [:a {:href   "https://www.dw.com/en/legal-notice/a-15718492"
                                :target "_blank"} "https://www.dw.com/en/legal-notice/a-15718492"]]

   ])
