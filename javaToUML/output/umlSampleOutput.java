@startuml


class BlogEntry {
-name:String
-contents:String
}

BlogEntry "0..*" -- "1..*" BlogAccount
(BlogEntry , BlogAccount) . Enrollment

class BlogAccount {
-name:String
+drop(name:String,count:int):boolean
}

interface foo {
+drop():boolean
}



@enduml