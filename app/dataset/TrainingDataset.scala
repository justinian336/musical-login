package dataset

import java.util.UUID

import distance.Distance
import lof.{LOFDataPoint, LOFDataSource}
import shapeless.ops.hlist.{LeftFolder, Mapper, Zip}
import shapeless.{Generic, HList, HNil}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

object TrainingDataset {

  case class TypingData(arg0: BigDecimal,
                        arg1: BigDecimal,
                        arg2: BigDecimal,
                        arg3: BigDecimal,
                        arg4: BigDecimal,
                        arg5: BigDecimal,
                        arg6: BigDecimal,
                        arg7: BigDecimal,
                        arg8: BigDecimal,
                        arg9: BigDecimal,
                        arg10: BigDecimal,
                        arg11: BigDecimal,
                        arg12: BigDecimal
                       ) extends LOFDataPoint[TypingData]{
    val id: UUID = UUID.randomUUID()
  }

  object TypingData{


    def fromList(data: List[BigDecimal]): TypingData = {

      TypingData(
        data.head,
        data(1),
        data(2),
        data(3),
        data(4),
        data(5),
        data(6),
        data(7),
        data(8),
        data(9),
        data(10),
        data(11),
        data(12)
      )

    }

  }

  case class TypingDataset(data: mutable.MutableList[TypingData]) extends LOFDataSource[TypingData]{


    /**
      * A custom implementation of k-nearest neighbor search. The speed of the LOF and LoOP implementations greatly depends
      * on the efficiency of this method.
      *
      * @param p a data point
      * @param k the size of the neighborhood
      * @return
      */
    override def getKNN[H <: HList, K <: HList, L <: HList](p: TypingData, k: Int)(implicit dataSource: LOFDataSource[TypingData], ec: ExecutionContext, gen: Generic.Aux[TypingData, H], zipper: Zip.Aux[shapeless.::[H, shapeless.::[H, HNil]], L], diffMapper: Mapper.Aux[Distance.absDiffMap.type, L, H], folder: LeftFolder.Aux[H, BigDecimal, Distance.reducerPoly.type, BigDecimal]): Future[(LOFDataPoint[TypingData], List[(TypingData, BigDecimal)], Int)] = {
      val neighborhood = data.par.map{neighbor=>
        neighbor -> neighbor.distance(p)
      }.toList.sortBy{case (_, dist) =>
        dist
      }.take(k)

      Future.successful {
        (p, neighborhood, k)
      }
    }


    /**
      * Returns the mean Probability Local Outlier Factor. Necessary for LoOP calculation
      *
      * @param k the size of the neighborhood
      * @param p the data point
      * @param λ the confidence level as a Normal Distribution z value
      * @return
      */
    override def getNPLOF[H <: HList, K <: HList, L <: HList](k: Int, p: TypingData, λ: BigDecimal)(implicit dataSource: LOFDataSource[TypingData], ec: ExecutionContext, gen: Generic.Aux[TypingData, H], zipper: Zip.Aux[shapeless.::[H, shapeless.::[H, HNil]], L], diffMapper: Mapper.Aux[Distance.absDiffMap.type, L, H], folder: LeftFolder.Aux[H, BigDecimal, Distance.reducerPoly.type, BigDecimal]): Future[BigDecimal] = {
      Future.sequence(
        data.map(_.getPLOF(k, λ))
      ).map(_.foldLeft(BigDecimal.valueOf(0)){_ + _})
    }

    def appendPoint(typingData: TypingData) = {
      data += typingData
    }
}

  val data = TypingDataset(mutable.MutableList())

}
