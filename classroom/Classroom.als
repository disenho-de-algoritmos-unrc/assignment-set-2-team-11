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
	// TODO: Specify this property
}

/* There are no teachers. */
pred inv2 {
    // TODO: Specify this property	
}

/* No person is both a student and a teacher. */
pred inv3 {
    // TODO: Specify this property
}

/* No person is neither a student nor a teacher. */
pred inv4 {
    // TODO: Specify this property	
}

/* There are some classes assigned to teachers. */
pred inv5 {
    // TODO: Specify this property	
}

/* Every teacher has classes assigned. */
pred inv6 {
    // TODO: Specify this property	
}

/* Every class has teachers assigned. */
pred inv7 {
    // TODO: Specify this property	
}

/* Teachers are assigned at most one class. */
pred inv8 {
    // TODO: Specify this property	
}

/* No class has more than a teacher assigned. */
pred inv9 {
    // TODO: Specify this property	
}

/* For every class, every student has a group assigned. */
pred inv10 {
    // TODO: Specify this property	
}

/* A class only has groups if it has a teacher assigned. */
pred inv11 {
    // TODO: Specify this property	
}

/* Each teacher is responsible for some groups. */
pred inv12 {
    // TODO: Specify this property	
}

/* Only teachers tutor, and only students are tutored. */
pred inv13 {
    // TODO: Specify this property	
}

/* Every student in a class is at least tutored by all the teachers
 * assigned to that class. */
pred inv14 {
    // TODO: Specify this property	
}
}

/* The tutoring chain of every person eventually reaches a Teacher. */
pred inv15 {
    // TODO: Specify this property	
}
