open util/ordering[Fecha] as ord

sig Equipo { 
	local: some Partido,
	visitante: some Partido
} 

sig Partido {
	Juegan: Equipo -> Equipo
}

sig Fecha {
	partidos: some Partido
}

sig Fixture {
	fechas: some Fecha
}

//En un partido participan solo dos equipos
pred cantEquiposPartido[] {
	 all p : Partido |  #(p.Juegan) = 1
}

//Ningun equipo puede jugar un partido contra sigo mismo
pred restriccionPartido[] {
	all p : Partido | all e1, e2 : Equipo | (e1 -> e2) in p.Juegan => e1 != e2
}

//La cantidad de equipos es par
pred esPar[] {
	some k : Int | #Equipo = mul[2, k]
}

//Cada fecha tiene k/2 cantidad de partidos, con k la cantidad de equipos
pred cantPartidosFecha[] {
	all f : Fecha | #f.partidos = div[#Equipo, 2]
}


//todas las fechas deben pertenecer a algun fixture
pred tenerFecha[] {
	all f : Fecha | some f1 : Fixture | f in f1.fechas
}

assert controlarFecha {
	tenerFecha => no f : Fecha | some f1 : Fixture | f !in f1.fechas
}

//no se juega el mismo partido en mas de una fecha
pred partidosUnicos[] {
	all p : Partido | one f : Fecha | p in f.partidos
}

//no hay dos partidos distintos que sean iguales
pred partidosDistintos[] {
	all p1, p2 : Partido | p1 != p2 => p1.Juegan & p2.Juegan = none -> none
}


//no puede haber un mismo partido en mas de una fecha
pred noPartidosRepetidos[] {
	all p : Partido | one f : Fecha | p in f.partidos
}


//si un equipo juega contra otro de local, no pueden volver a jugar con la localia invertida
pred cambiarLocalia[] {
	all p1, p2 : Partido | p1 != p2 => ~(p1.Juegan) != p2.Juegan and ~(p2.Juegan) != p1.Juegan
}

//un equipo no puede jugar mas de un partido en la misma fecha
pred unPartidoPorFecha[] {
	all p1, p2 : Partido | all f : Fecha | all e : Equipo |
	p1 in f.partidos and p2 in f.partidos and p1 != p2 and 
	((e -> Equipo) & p1.Juegan != (none -> none) iff not (Equipo -> e) & p1.Juegan != (none -> none)) =>
	((e -> Equipo) & p2.Juegan = (none -> none) and (Equipo -> e) & p2.Juegan = (none -> none))
}

//En un partido e1 -> e2, e1 es el local
pred esLocal[] {
	all p : Partido | all e : Equipo | ((e -> Equipo) & p.Juegan != (none -> none)) iff p in e.local
}

//En un partido e1 -> e2, e2 es ek visitante
pred esVisitante[] {
	all p : Partido | all e : Equipo | ((Equipo -> e) & p.Juegan != (none -> none)) iff p in e.visitante
}

//Cada equipo juega k/2 partidos de local y k/2 - 1 de visitante (o al reves), con
// k la cantidad de equipos
pred cantLocalVisitante[] {
	all e : Equipo | (#(e.local) = div[#Equipo, 2]  and #(e.visitante) = minus[div[#Equipo, 2], 1]) iff not 
	(#(e.visitante) = div[#Equipo, 2] and #(e.local) = minus[div[#Equipo, 2], 1])
}

//hay exactamente una fecha en la cual se da que un equipo juega de local en ella y en la
// siguiente (o de visitante)
pred localVisitante[] {
	all e : Equipo | one f : Fecha | let f' = f.(ord/next) { 
		(e.local & f.partidos != none and e.local & f'.partidos != none) iff not
		(e.visitante & f.partidos != none and e.visitante & f'.partidos != none)
	}
}


fact {
	cantEquiposPartido[]
	restriccionPartido[]
	esPar[]
	cantPartidosFecha[]
	tenerFecha[]
	partidosDistintos[]
	partidosUnicos[]
	noPartidosRepetidos[]
	cambiarLocalia[]
	unPartidoPorFecha[]
	esLocal[]
	esVisitante[]
	cantLocalVisitante[]
	localVisitante[]
	#Fecha = minus[#Equipo,1]
}

pred show1[] { }
run show1 for exactly 4 Equipo, 3 Fecha, 6 Partido, 1 Fixture

pred show2[] { }
run show2 for exactly 6 Equipo, 5 Fecha, 15 Partido, 1 Fixture
