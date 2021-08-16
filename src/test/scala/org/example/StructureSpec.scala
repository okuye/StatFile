package org.example

import org.example.Structure.getRankedData
import org.junit.Test
import org.scalatest.Assertions

class StructureSpec extends Assertions {

  @Test def getTheRankedDefenceDataCount() {
    val resourcesPath = getClass.getResource("/defence-test.xml")
    val ranked = getRankedData("defence", resourcesPath.getPath)
    println(s"ranked count : ${ranked.count()}")
    ranked.show(false)

    assert(ranked.count() === 11)

  }

}
