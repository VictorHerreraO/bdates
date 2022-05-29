package com.soyvictorherrera.bdates.core.arch

object MapperTestImpl : Mapper<Int, String>() {

    override fun map(value: Int): String = value.toString()

    override fun reverseMap(value: String): Int = value.toInt()

}
