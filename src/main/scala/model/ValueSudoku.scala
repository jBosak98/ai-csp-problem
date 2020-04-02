package model

class ValueSudoku(var value:Option[Int], var domain:List[Int], val isConstant:Boolean){

  def this(value:Option[Int], domain:List[Int]){
    this(isConstant = false, value=value, domain=domain)
  }
  def this(value:Option[Int], isConstant:Boolean){
    this(isConstant = isConstant, value=value, domain=Nil)
  }
  def this(value:Option[Int]){
    this(isConstant = false, value=value, domain=Nil)
  }
  def this(){
    this(isConstant = false, value=Option.empty[Int], domain=Nil)
  }

  override def toString: String = s"model.ValueSudoku(value=${value}, domain=${domain},isConstant=${isConstant})"
}
