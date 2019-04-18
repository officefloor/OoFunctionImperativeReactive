class Function {

  // START SNIPPET: function
  type Function = Array[Any] => Any
  // END SNIPPET: function

  // START SNIPPET: invoke
  type Executor = (Function, Array[Any]) => Any

  def invoke(executor: Executor, function: Function, parameters: Array[Any]) =
    executor(function, parameters)
  // END SNIPPET: invoke

  def functionIO(parameters: Array[Any]) = "io"
  def functionCpuIntensive(parameters: Array[Any]) = "cpu"
  // START SNIPPET: higher
  def higher(executorIO: Executor, executorCpuIntensive: Executor, parameters: Array[Any]) =
    executorIO(functionIO, Array(executorCpuIntensive(functionCpuIntensive, parameters)) ++ parameters)
  // END SNIPPET: higher

  // START SNIPPET: executorLocator
  type ExecutorLocator = Function => Executor
  // END SNIPPET: executorLocator

  // START SNIPPET: higherWithLocator
  def higher(executorLocator: ExecutorLocator, parameters: Array[Any]) =
    executorLocator(functionIO)(functionIO, Array(executorLocator(functionCpuIntensive)(functionCpuIntensive, parameters)) ++ parameters)
  // END SNIPPET: higherWithLocator

  // START SNIPPET: continuation
  type Continuation = (Any) => Unit

  def function(executorLocator: ExecutorLocator, parameters: Array[Any], continuation: Continuation)
  // END SNIPPET: continuation

  // START SNIPPET: errorContinuations
  def function(executorLocator: ExecutorLocator, parameters: Array[Any], successfulContinuation: Continuation, errorOneContinuation: Continuation, errorTwoContinuation: Continuation)
  // END SNIPPET: errorContinuations

  // START SNIPPET: multipleContinuations
  def function(executorLocator: ExecutorLocator, parameters: Array[Any], trueContinuation: Continuation, falseContinuation: Continuation, errorOneContinuation: Continuation, errorTwoContinuation: Continuation)
  // END SNIPPET: multipleContinuations

  // START SNIPPET: continuationLocator
  type ContinuationLocator = (Function, Any) => Continuation
  // END SNIPPET: continuationLocator

  // START SNIPPET: continuationFunction
  def function(executorLocator: ExecutorLocator, parameters: Array[Any], continuationLocator: ContinuationLocator)
  // END SNIPPET: continuationFunction

  object One {
    // START SNIPPET: functionWithContinuation
    class ManagedFunction(
      val logic: Function,
      val continuations: Map[Any, Continuation])
    // END SNIPPET: functionWithContinuation
  }

  // START SNIPPET: explodedFunction
  def function(parameters: Array[Any], trueContinuation: Continuation, falseContinuation: Continuation)
  // END SNIPPET: explodedFunction

  object Two {
    // START SNIPPET: runContinuation
    class ManagedFunction(
      val logic: Function,
      val continuations: Map[Any, Continuation]) {
      def cont(key: Any) = continuations.get(key) match { case Some(cont) => cont }

      def run(parameters: Array[Any]) = logic(parameters ++ Array(cont(1), cont(2)))
    }
    // END SNIPPET: runContinuation
  }

  object Three {
    // START SNIPPET: catchException
    class ManagedFunction(
      val logic: Function,
      val continuations: Map[Any, Continuation]) {
      def cont(key: Any) = continuations.get(key) match { case Some(cont) => cont }

      def run(parameters: Array[Any]) = {
        try {
          logic(parameters ++ Array(cont(1), cont(2)))
        } catch {
          case ex: Throwable => cont(ex.getClass())(ex)
        }
      }
    }
    // END SNIPPET: catchException
  }

  // START SNIPPET: extractContinuations
  def extractContinuations(function: Function) = {
    val keys: List[Any] = List()
    // reflectively check parameters for Continuations adding their index as key
    // reflectively check exceptions and add exception type as key
    keys
  }
  // END SNIPPET: extractContinuations

  object Four {
    // START SNIPPET: singleParameter
    class ManagedFunction {
      def run(parameter: Any) // not, parameters: Array[Any]
    }
    // END SNIPPET: singleParameter
  }

  // START SNIPPET: managedObject
  class ManagedObject(
    @Inject val continuationOne: Continuation,
    @Inject val continuationTwo: Continuation) {
    // ... object methods
  }
  // END SNIPPET: managedObject

  // START SNIPPET: dependencyContext
  type ServiceLocator = String => Any

  class DependencyContext(val serviceLocator: ServiceLocator) {
    val objects = scala.collection.mutable.Map[String, Any]()

    def getObject(name: String) = {
      objects.get(name) match {
        case Some(obj) => obj
        case None => {
          val obj = serviceLocator(name)
          objects(name) = obj
          obj
        }
      }
    }
  }
  // END SNIPPET: dependencyContext

  // START SNIPPET: continuationFactory
  type ContinuationFactory = DependencyContext => Continuation
  // END SNIPPET: continuationFactory

  object Five {
    // START SNIPPET: functionDependencyContext
    class ManagedFunction(
      val logic: Function,
      val parameterScopeNames: List[String],
      val continuations: Map[Any, ContinuationFactory]) {
      def obj(index: Int, context: DependencyContext) = context.getObject(parameterScopeNames(index))
      def cont(key: Any, context: DependencyContext) = continuations.get(key) match { case Some(factory) => factory(context) }

      def run(parameterFromContinuation: Any, context: DependencyContext) = {
        try {
          logic(Array(parameterFromContinuation, obj(1, context), obj(2, context), cont(1, context), cont(2, context)))
        } catch {
          case ex: Throwable => cont(ex.getClass(), context)(ex)
        }
      }
    }
    // END SNIPPET: functionDependencyContext
  }

  // START SNIPPET: variables
  trait Out[T] { def set(value: T) }
  trait In[T] { def get(): T }
  trait Var[T] extends Out[T] with In[T]
  // END SNIPPET: variables

  // START SNIPPET: parameters
  def logicArguments(logic: Function, parameterFromContinuation: Any, obj: (Int, DependencyContext) => Any, cont: (Any, DependencyContext) => Continuation): Array[Any] = {
    var arguments = Array[Any]()
    // reflectively interrogate logic function to create argument for each parameter
    arguments
  }
  // Note: as the logic function signature is static, this function can be generated compile time avoiding reflection for performance
  // END SNIPPET: parameters

  object Six {
    // START SNIPPET: unorderedParameters
    class ManagedFunction(
      val logic: Function,
      val parameterScopeNames: List[String],
      val continuations: Map[Any, ContinuationFactory]) {
      def obj(index: Int, context: DependencyContext) = context.getObject(parameterScopeNames(index))
      def cont(key: Any, context: DependencyContext) = continuations.get(key) match { case Some(factory) => factory(context) }

      def run(parameterFromContinuation: Any, context: DependencyContext) = {
        try {
          logic(logicArguments(logic, parameterFromContinuation, obj, cont))
        } catch {
          case ex: Throwable => cont(ex.getClass(), context)(ex)
        }
      }
    }
    // END SNIPPET: unorderedParameters
  }

  class ParameterType
  // START SNIPPET: executor
  def executorLocator(logic: Function, configuration: Map[ParameterType, Executor]): Executor = {
    // Default executor is synchronous (implicit thread)
    var executor: Executor = (logic, arguments) => logic(arguments)
    // reflectively interrogate logic function to look up Executor by the parameter types within the configuration
    // (if no match then does not override executor and uses implicit thread)
    executor
  }
  // Note: as the logic function signature is static, memoization (caching Executor for ManagedFunction) can improve performance
  // END SNIPPET: executor

  object Seven {
    def createManagedFunction(): ManagedFunction = null
    // START SNIPPET: managedFunction
    class ManagedFunction(
      val logic: Function,
      val executor: Executor,
      val parameterScopeNames: List[String],
      val continuations: Map[Any, ContinuationFactory]) {
      
      def obj(index: Int, context: DependencyContext) = context.getObject(parameterScopeNames(index))
      def cont(key: Any, context: DependencyContext) = continuations.get(key) match { case Some(factory) => factory(context) }

      def run(parameterFromContinuation: Any, context: DependencyContext) = {
        executor((arguments) => {
          try {
            logic(arguments)
          } catch {
            case ex: Throwable => cont(ex.getClass(), context)(ex)
          }
        }, logicArguments(logic, parameterFromContinuation, obj, cont))
      }
    }
    // END SNIPPET: managedFunction
  }

}