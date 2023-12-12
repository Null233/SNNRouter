// See README.md for license details.

package gcd

import chisel3._
import chisel3.util._

// class DataReg(width : Int) extends Module{
//   val io = IO(new Bundle{
//     val din = Input(UInt(width.W))
//     val validIn = Input(Bool())
//     val dout = Output(UInt(width.W))
//     val validOut = Input(Bool())
//   })

//   def dataReg[T <: Data](width : Int) = {
//     new Bundle{
//       RegNext(io.din, 0.U)
//       RegNext(io.validIn, false.B)
//     }
//   }

//   io.dout := RegNext(io.din, 0.U)
//   io.validOut := RegNext(io.validIn, false.B)
// }

// class DataReg[T <: Data](private val data : T, private val valid : Bool, width : Int) extends Bundle{
//   val dataReg = RegInit(data.cloneType, 0.U(width.W))
//   val validReg = RegInit(valid, false.B)
//   def dataReg = {
//     val datareg = new Bundle{
//       RegNext(data, 0.U)
//       RegNext(valid, false.B)
//     }
//   }
// }

class FIFO(width : Int, length : Int) extends Module{
  val io = IO(new Bundle{
    val input = Flipped(DecoupledIO(UInt(width.W)))
    val output = DecoupledIO(UInt(width.W))
  })
  // val regs = Seq.fill(length){new DataReg(0.U(width.W), false.B, width)}
  // // val regs = Vec(length, new DataReg(0.U(width.W), false.B, width))
  // io.input.ready := VecInit(regs).map((x : DataReg[UInt]) => x.validReg)

  // val dataRegs = Vec.fill(length) {RegInit(0.U(width.W))}
  val dataRegs = VecInit(Seq.fill(length)(RegInit(0.U(width.W))))
  val counter = RegInit(0.U(width.W))
  io.input.ready := ! (counter === length.U)
  io.output.valid := ! (counter === 0.U)
  when(io.input.valid){
    dataRegs(counter) := io.input.bits
    counter := counter + 1.U
  }
  io.output.valid := counter =/= 0.U
  io.output.bits := Mux(io.output.ready, dataRegs(counter), 0.U(width.W))

}

object FSM extends App{
  emitVerilog(new FIFO(32, 10), Array("--target-dir", "generated"))
}