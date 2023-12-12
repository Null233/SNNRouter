package gcd

import chisel3._
import chisel3.util._

class RisingMooreFsm extends Module {
    val io = IO(new Bundle {
        val d = Input(Bool())
        val q = Output(Bool())
    })

    io.q := RegNext(io.d, false.B)

}