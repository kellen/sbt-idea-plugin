package org.jetbrains.sbtidea.download.jbr

import java.net.URL
import java.nio.file.Path
import org.jetbrains.sbtidea.Keys.JbrInfo
import org.jetbrains.sbtidea.download.BuildInfo
import org.jetbrains.sbtidea.download.api._

case class JbrDependency(ideaRoot: Path,
                          buildInfo: BuildInfo,
                          jbrInfo: JbrInfo,
                          dependsOn: Seq[UnresolvedArtifact] = Seq.empty) extends UnresolvedArtifact {

override type U = JbrDependency
  override type R = JbrArtifact
  override protected def usedResolver: JbrBintrayResolver = new JbrBintrayResolver
  override def toString: String = s"JbrDependency($jbrInfo)"
}

case class JbrArtifact(caller: JbrDependency, dlUrl: URL) extends ResolvedArtifact with UrlBasedArtifact {
  override type R = JbrArtifact
  override protected def usedInstaller: JbrInstaller = new JbrInstaller
}