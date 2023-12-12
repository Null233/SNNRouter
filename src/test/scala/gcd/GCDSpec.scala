// See README.md for license details.

package gcd

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random

class RisingEdgeTest extends AnyFlatSpec with ChiselScalatestTester {
  "RisingEdge" should "pass" in {
      test(new RisingMooreFsm)
        .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
            dut.io.d.poke(true.B)
            dut.io.d.poke(false.B)
            dut.io.d.poke(true.B)
            dut.clock.step()
            dut.io.d.poke(false.B)
            dut.clock.step(5)
        }
  }
}