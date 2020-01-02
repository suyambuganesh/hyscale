/**
 * Copyright 2019 Pramati Prism, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hyscale.deployer.services.processor;

import io.hyscale.commons.component.IInterceptorProcessor;
import io.hyscale.commons.exception.HyscaleException;
import io.hyscale.commons.models.DeploymentContext;

public abstract class DeployerInterceptorProcessor implements IInterceptorProcessor {

    abstract protected void _around(DeploymentContext context) throws HyscaleException;

    abstract protected void _before(DeploymentContext context) throws HyscaleException;

    abstract protected void _afterRunning(DeploymentContext context) throws HyscaleException;

    abstract protected void _afterThrowing(DeploymentContext context) throws HyscaleException;

    @Override
    public void around(Object... args) throws HyscaleException {
        if (!isInputValid(args)) {
            return;
        }
        _around((DeploymentContext) args[0]);
    }

    @Override
    public void before(Object... args) throws HyscaleException {
        if (!isInputValid(args)) {
            return;
        }
        _before((DeploymentContext) args[0]);
    }

    @Override
    public void afterRunning(Object... args) throws HyscaleException {
        if (!isInputValid(args)) {
            return;
        }
        _afterRunning((DeploymentContext) args[0]);
    }

    @Override
    public void afterThrowing(Object... args) throws HyscaleException {
        if (!isInputValid(args)) {
            return;
        }
        _afterThrowing((DeploymentContext) args[0]);
    }

    private boolean isInputValid(Object... args) {
        if (args == null || args.length < 1) {
            return false;
        }
        if (args[0] == null || !(args[0] instanceof DeploymentContext)) {
            return false;
        }
        return true;
    }

}
