import com.google.inject.{AbstractModule, Provides}
import com.noteinweb.paas.api.v1.controlplane.{ControlPlaneExecutionContext, ControlPlaneRepository, ControlPlaneRepositoryImpl}
import com.noteinweb.paas.cloud.act.CloudActor
import com.noteinweb.paas.cloud.aws.{AWSCloudInspector, AWSCloudTransformer, AwsCloudActor}
import com.noteinweb.paas.cloud.inspect.CloudInspector
import com.noteinweb.paas.cloud.transform.CloudTransformer
import com.noteinweb.paas.dsl.InjectableAstInterpreterImpl
import com.noteinweb.paas.dsl.interpreter.{ASTInterpreter, ASTInterpreterImpl}
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment, Logger}

import javax.inject._



class Module(environment: Environment, configuration: Configuration)
      extends AbstractModule with ScalaModule {
  private val logger = Logger(getClass)

  override def configure(): Unit = {
    logger.info("Initializing DI")
    bind[ControlPlaneRepository].to[ControlPlaneRepositoryImpl].in[Singleton]()
    bind[ASTInterpreter].to[InjectableAstInterpreterImpl].in[Singleton]()
    bind[CloudInspector].to[AWSCloudInspector].in[Singleton]()
    bind[CloudTransformer].to[AWSCloudTransformer].in[Singleton]()
    bind[ControlPlaneExecutionContext]
    //bind[List[+CloudActor]].to[List[CloudActor]AwsCloudActor()].in[Singleton]()
    //bind[CloudActor].to[AWSCloudActorImpl].in[Singleton]()
    logger.info("DONE - Initializing DI")
  }

  @Provides
  def transformers() : List[CloudTransformer] = {
    List( new AWSCloudTransformer() )
  }

  @Provides
  def inspectors() : List[CloudInspector] = {
    List ( new AWSCloudInspector() )
  }

  @Provides
  def actors() : List[CloudActor] = {
    List ( new AwsCloudActor() )
  }
}
