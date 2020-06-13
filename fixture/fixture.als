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

pred cantEquiposPartido[] {
	 all p : Partido |  #(p.Juegan) = 1
}


pred restriccionPartido[] {
	all p : Partido | all e1, e2 : Equipo | (e1 -> e2) in p.Juegan => e1 != e2
}


pred esPar[] {
	some k : Int | #Equipo = mul[2, k]
}

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

//todos los equipos juegan un partido en todas las fechas
pred juegan[] {
	all e : Equipo | all f : Fecha | one p : Partido | p in f.partidos and (e -> Equipo) in ~(p.Juegan)
	
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
	((e -> univ) & p1.Juegan != (none -> none) iff not (univ -> e) & p1.Juegan != (none -> none)) =>
	((e -> univ) & p2.Juegan = (none -> none) and (univ -> e) & p2.Juegan = (none -> none))
}

pred esLocal[] {
	all p : Partido | all e : Equipo | ((e -> univ) & p.Juegan != (none -> none)) iff p in e.local
}

pred esVisitante[] {
	all p : Partido | all e : Equipo | ((univ -> e) & p.Juegan != (none -> none)) iff p in e.visitante
}

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
	#Fecha = minus[#Equipo, 1]
}

pred show[] { }
run show for 6 Equipo, 5 Fecha, 15 Partido, 1 Fixture
