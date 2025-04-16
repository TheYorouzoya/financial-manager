file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java
### java.lang.AssertionError: assertion failed: position not set for <error> # -1 of class dotty.tools.dotc.ast.Trees$Ident in <WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java
text:
```scala
package com.ratnesh.financialmanager.config;

import org.springframework.security.web.AuthenticationEntryPoint;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    
    @
}

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.typer.Typer$.assertPositioned(Typer.scala:72)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3272)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.typedType(Typer.scala:3395)
	dotty.tools.dotc.typer.Namer.typedAheadType$$anonfun$1(Namer.scala:1701)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1694)
	dotty.tools.dotc.typer.Namer.typedAheadType(Namer.scala:1701)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1710)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1709)
	dotty.tools.dotc.typer.Namer.typedAheadAnnotationClass(Namer.scala:1707)
	dotty.tools.dotc.typer.Namer$Completer.addAnnotations$$anonfun$1(Namer.scala:850)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.typer.Namer$Completer.addAnnotations(Namer.scala:849)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:952)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:828)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:174)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:188)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:190)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:392)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:3067)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3092)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3206)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3303)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3349)
	dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:2746)
	dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$1(Typer.scala:3114)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3118)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3206)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3303)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3349)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2889)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3159)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3207)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3277)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3281)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3392)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:45)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:463)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:51)
	dotty.tools.dotc.typer.TyperPhase.$anonfun$4(TyperPhase.scala:97)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:583)
	scala.collection.immutable.List.prependedAll(List.scala:152)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:900)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:96)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:315)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:308)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:349)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:358)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:358)
	dotty.tools.dotc.Run.compileSources(Run.scala:261)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:161)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position not set for <error> # -1 of class dotty.tools.dotc.ast.Trees$Ident in <WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java