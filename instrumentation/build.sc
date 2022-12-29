import mill._
import scalalib._
import publish._

object ivys {
  val sv = "2.12.17"
  val chisel3 = ivy"edu.berkeley.cs::chisel3:3.5.5"
  val chisel3Plugin = ivy"edu.berkeley.cs:::chisel3-plugin:3.5.5"
  val macroParadise = ivy"org.scalamacros:::paradise:2.1.1"
}

object rfuzz extends ScalaModule with PublishModule with SbtModule {
  def sourceRoot = T.sources { T.workspace / "src" }

  def allSources = T { sourceRoot().flatMap(p => os.walk(p.path)).map(PathRef(_)) }

  // override this to use chisel from source
  def chiselOpt: Option[PublishModule] = None

  override def scalaVersion = ivys.sv

  override def compileIvyDeps = Agg(ivys.macroParadise)

  override def scalacPluginIvyDeps = Agg(ivys.macroParadise, ivys.chisel3Plugin)

  override def scalacOptions = Seq("-Xsource:2.11")

  override def ivyDeps = (if(chiselOpt.isEmpty) Agg(ivys.chisel3) else Agg.empty[Dep])

  override def moduleDeps = Seq() ++ chiselOpt

  def publishVersion = "0.0.1"

  def pomSettings = PomSettings(
    description = "rfuzz-instrumentation",
    organization = "edu.berkeley.cs",
    url = "https://github.com/ekiwi/rfuzz",
    licenses = Seq(License.`BSD-3-Clause`),
    versionControl = VersionControl.github("ekiwi", "rfuzz"),
    developers = Seq(
      Developer("ekiwi", "Kevin Laeufer", "https://github.com/ekiwi")
    )
  )
}
