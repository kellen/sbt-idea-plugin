package org.jetbrains.sbtidea.download.plugin

import org.jetbrains.sbtidea.IntellijPlugin.*
import org.jetbrains.sbtidea.Keys.String2Plugin
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.net.URL
import scala.language.postfixOps

final class IntellijPluginParserTest extends AnyFunSuite with Matchers {

  private def id(str: String) = str.toPlugin.asInstanceOf[Id]

  test("Id parser should parse simple id") {
    "org.jetbrains.scala".toPlugin shouldBe a [Id]
  }

  test("plugin url should parse as Url") {
    "https://foo.bar/a.zip".toPlugin shouldBe an [Url]
    "http://foo.bar/a.zip".toPlugin shouldBe an [Url]
  }

  test("plugin with url should parse as Id with optional URL set") {
    val p = "org.example.plugin:http://foo.bar/a.zip".toPlugin
    p shouldBe an [Id]
    p.asInstanceOf[Id].url shouldEqual Some(new URL("http://foo.bar/a.zip"))

    val p1 = "org.example.plugin:1.0-api-212:https://foo.bar/a.zip".toPlugin
    p1 shouldBe an [Id]
    p1.asInstanceOf[Id].url shouldEqual Some(new URL("https://foo.bar/a.zip"))
    p1.asInstanceOf[Id].version shouldEqual Some("1.0-api-212")
  }

  test("id should parse with with mixed segments") {
    id("foo").id shouldBe "foo"
    id("foo:").id shouldBe "foo"
    id("foo::").id shouldBe "foo"
    id("foo::baz").id shouldBe "foo"
    id("foo:123:").id shouldBe "foo"
    id("foo:123:baz").id shouldBe "foo"
  }

  test("parser should not parse invalid strings") {
    assertThrows[RuntimeException]("::".toPlugin)
    assertThrows[RuntimeException](":123:foo".toPlugin)
    assertThrows[RuntimeException](":123:".toPlugin)
  }
}
