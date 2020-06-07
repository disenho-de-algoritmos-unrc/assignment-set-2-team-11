/* The registered persons. */
sig Person  {
	/* Each person tutors a set of persons. */
	Tutors : set Person,
	/* Each person teaches a set of classes. */
	Teaches : set Class
}

/* The registered groups. */
sig Group {}

/* The registered classes. */
sig Class  {
	/* Each class has a set of persons assigned to a group. */
	Groups : Person -> Group
}

/* Some persons are teachers. */
sig Teacher extends Person  {}

/* Some persons are students. */
sig Student extends Person  {}

/* Every person is a student. */
pred inv1 {
	 all x : Person | x in Student
}

run inv1

/* There are no teachers. */
pred inv2 {
    all x : Person | x !in Teacher	
}

run inv2


/* No person is both a student and a teacher. */
pred inv3 {
    all x : Person | x in Student iff not x in Teacher
}

run inv3

/* No person is neither a student nor a teacher. */
pred inv4 {
    all x : Person | x !in Student and x !in Teacher 	
}

run inv4

/* There are some classes assigned to teachers. */
pred inv5 {
    some c : Class | some t : Teacher | c in t.Teaches
}


run inv5

/* Every teacher has classes assigned. */
pred inv6 {
    all x : Teacher | x.Teaches != none	
}

assert assertInv6 {
	all t : Teacher | inv6 implies some c : Class | c in t.Teaches
}

check assertInv6

run inv6

/* Every class has teachers assigned. */
pred inv7 {
    all x : Class | some y : Teacher | x in y.Teaches	
}

run inv7

/* Teachers are assigned at most one class. */
pred inv8 {
    all x : Teacher | lone y : Class | y in x.Teaches	
}

run inv8

/* No class has more than a teacher assigned. */
pred inv9 {
	lone x : Teacher | all y : Class | y in x.Teaches
}

run inv9

/* For every class, every student has a group assigned. */
pred inv10 {
    all c : Class | all s : Student | one g : Group | (s -> g) in (c.Groups)
}

run inv10

/* A class only has groups if it has a teacher assigned. */
pred inv11 {
    	all c : Class | some t : Teacher | c !in t.Teaches  implies
	c.Groups = (none -> none)
}

assert hola {
	inv11 implies no c : Class | some t : Teacher | c !in t.Teaches and c.Groups != (none -> none)
}

check hola

run inv11

/* Each teacher is responsible for some groups. */
pred inv12 {
    some c : Class | all t : Teacher | some g : Group | (t -> g) in c.Groups
}

run inv12

/* Only teachers tutor, and only students are tutored. */
pred inv13 {
    all p, p' : Person | p in p'.Tutors implies p in Student and p' in Teacher
}

run inv13

/* Every student in a class is at least tutored by all the teachers
 * assigned to that class. */
pred inv14 {
    // TODO: Specify this property	
}


/* The tutoring chain of every person eventually reaches a Teacher. */
pred inv15 {
    // TODO: Specify this property	
}
