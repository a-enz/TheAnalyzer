/*  ceilings and floors */
open util/boolean
open util/integer

sig Platform {
}


sig Man {
  ceiling, floor: one Platform,
  between: Platform some -> one ceiling
}

pred Above[m, n: one Man] {
  m.floor = n.ceiling
}

assert BelowToo { all m: Man | one n: Man | m.Above[n] }

check BelowToo for 2 expect 1

/* dates */

sig Date {}
fun Closure[date: Date -> Date]: Date -> Date {^date}

/* genealogy */

abstract sig Person {}
sig Woman extends Person {}
one sig Eve extends Woman {}
