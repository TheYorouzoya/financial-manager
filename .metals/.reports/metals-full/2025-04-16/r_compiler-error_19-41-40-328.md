file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 348
uri: file://<WORKSPACE>/src/main/java/com/ratnesh/financialmanager/config/CustomAuthEntryPoint.java
text:
```scala
package com.ratnesh.financialmanager.config;

import org.springframework.security.web.AuthenticationEntryPoint;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthentiationException au@@
    )
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:72)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator