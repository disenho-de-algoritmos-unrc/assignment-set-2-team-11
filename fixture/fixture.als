sig Equipo { } 

sig Partido {
	local: one Equipo,
	visitante: one Equipo
}

sig Fecha {
	partidos: set Partido
}

sig Fixture {
	fechas: some Fecha
}

pred restriccionPartido[] {
	all p : Partido | p.local != p.visitante
}

pred esPar[] {
	some k : Int | #Equipo = mul[2, k]
}

pred cantFechas[] {
	#Fecha = minus[#Equipo, 1]
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
	all e : Equipo | all f : Fecha | one p : Partido |
	p in f.partidos and (p.local = e or p.visitante = e)
}

assert controlaParticipacion {
	juegan => no f : Fecha | no p : Partido | some e : Equipo | p in f.partidos && (p.local != e or p.visitante != e) 
}


//defien cuando dos partidos son distintos
pred defPartidosDistintos[p1 : Partido, p2 : Partido] {
	p1 != p2 iff p1.local != p2.local || p1.visitante != p2.visitante
}

//no se juegan dos partidos iguales
pred partidosDistintos[] {
	all p1, p2 : Partido | defPartidosDistintos[p1, p2]
}

//no puede haber un mismo partido en mas de una fecha
pred noPartidosRepetidos[] {
	all p : Partido | one f : Fecha | p in f.partidos
}

assert assertNoPartidosRepetidos {
	noPartidosRepetidos => no p : Partido | one f : Fecha | one f1: Fecha | p in f.partidos and p in f1.partidos and f != f1
}


//si un equipo juega contra otro de local, no pueden volver a jugar con la localia invertida
pred cambiarLocalia[] {
	all e1, e2 : Equipo | one p : Partido | (p.local = e1 and p.visitante = e2) => 
	no p' : Partido | (p'.local = e2 and p'.visitante = e1)
}

pred cantPartidosFecha[] {
 all f : Fecha | #f.partidos = div[#Equipo, 2]
}


fact {
	esPar[] and 
	cantFechas[] and
	juegan[] and 
	partidosDistintos[] and
	cambiarLocalia[] and 
	noPartidosRepetidos[] and
	tenerFecha[]
}


pred show[] { }
run show 
