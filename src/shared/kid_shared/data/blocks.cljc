(ns kid-shared.data.blocks)

;;
;; A datatype meant to be able to post certain news
;;  periodically
;;
(def web-search-explanation
  [:<>
   [:p.mb-3 "Sometimes a simple web search is all you need to verify a post."]
   [:p.mb-3 "Just bring up your favorite search engine and type in a couple of relevant keywords. These can be taken straight from the text (like the name of an institution) or describe the content on a meta level (report on XYZ)."]
   [:p.mb-3 "To get good results, it's important to put multiword terms in quotes (e.g. 'European Union') and also use Boolean operators (e.g. \"European Union\" AND \"climate report\")."]
   [:p.mb-3 "A search for the right keywords often leads you to either an original source or a secondary piece of content that refers to it. You should then compare the content of these sources with what people claim on social media and boom! – you get a good hunch of what's legit and what's not."]])

(def image-analysis-explanation
  [:<>
   [:p.mb-3 "An image analysis tells you whether or not an image has been tampered with. For a basic analysis, all the tools you need are your eyes."]
   [:p.mb-3 "Take a closer look at the image and ask yourself: Are there any weird looking spots? Is there something off about the perspective? About the lighting? The colors? Does it looks like objects or people have been added afterwards?"]
   [:p.mb-3 "If the answer to one or more of the above questions is \"yes\", you're probably looking at a manipulated image."]
   [:p.mb-3 "If you're not sure, use a digital zoom (or a set of forensics tools) to investigate further."]])

(def ris-explanation
  [:<>
   [:p.mb-3 "Among other things, an RIS tells you whether or not an image has been used on the (open) web before – and in which context."]
   [:p.mb-3 "To do an RIS hands down, install the so-called \"WeVerify Plugin\" for your browser. Once it’s activated, right-click on the web image you need to verify, choose a search engine (e.g. the one by Megacorp) – and wait for the RIS results to show up."]
   [:p.mb-3 "If the image you're verifying shows up in lots of other places on the web (especially with different dates and descriptions), it’s very likely you're NOT looking at an original."]
   [:p.mb-3 "Which means: People making weird claims about said image on Bleeper are probably NOT telling the truth."]])

(def ris-crop-explanation
  [:<>
   [:p.mb-3 "Sometimes, an RIS doesn't yield any results. In that case, it can be good idea to crop the picture, thus putting a focus on a certain part of it, e.g. a person or a building. Repeat the search. Refine the keywords if necessary."]
   [:p.mb-3 "Do the results show the person or building allegedly depicted in the uncropped version? If they don't, any claim about this image should be treated with utmost caution."]])

(def ris-flip-explanation
  [:<>
   [:p.mb-3 "Sometimes, an RIS doesn't yield any results. That may be because it has been inverted. Try flipping it again, then repeat the RIS."]])

(def game-tagline-title "Welcome to the KID Game")

(def game-tagline-welcome "You've heard about that whole \"fake news\" thing, haven't you?")

(def game-explanation
  [:<>
   [:p.mb-3 "We've created this little browser game to teach you the basics of verification. That's just a fancy term for thoroughly checking news items and social media messages that may contain false information – or worse."]
   [:p.mb-3 "Now as soon as you've entered your name and clicked on the \"login\" button, you'll be taken to \"Bleeper\". That's a fictitious social network in a not so distant future which might be more realistic than you like."]
   [:p.mb-3 "On the platform, you'll see a lot of posts sliding in and out of your timeline. In this demo, you can read all of them, and you’re free to interact with some of them -- hopefully after you've taken a closer look."]
   [:p.mb-3 "You'll get the hang of this soon. And if you don't, feel free to ask your friend Thomas: the duck!"]])
