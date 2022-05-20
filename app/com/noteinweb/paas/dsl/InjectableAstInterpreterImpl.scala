package com.noteinweb.paas.dsl

import com.noteinweb.paas.cloud.act.CloudActor
import com.noteinweb.paas.cloud.transform.CloudTransformer
import com.noteinweb.paas.dsl.interpreter.ASTInterpreterImpl

import javax.inject.Inject

class InjectableAstInterpreterImpl @Inject()(transformers: List[CloudTransformer], actors: List[CloudActor])
  extends ASTInterpreterImpl(transformers, actors) {}
