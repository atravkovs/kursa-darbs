/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.bpm.getstarted.loanapproval;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.getstarted.loanapproval.models.Complex;

import java.util.logging.Logger;

public class MandelbrotDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("MANDELBROT");

    public int calculate(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }

    public int[][] mandelbrot(int n, int max) {
        double xc = -0.5;
        double yc = 0.0;
        double size = 2.0;

        int[][] picture = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double x0 = xc - size / 2 + size * i / n;
                double y0 = yc - size / 2 + size * j / n;
                Complex z0 = new Complex(x0, y0);
                int colorIntensity = max - calculate(z0, max);
                picture[i][n - 1 - j] = colorIntensity;
            }
        }

        return picture;
    }

    public void execute(DelegateExecution execution) throws Exception {
        int n = Math.toIntExact((Long) execution.getVariable("imageSize"));
        int max = Math.toIntExact((Long) execution.getVariable("maxIterations"));

        LOGGER.info("[START] Mandelbrot calculation");

        mandelbrot(n, max);

        LOGGER.info("[END] Mandelbrot calculation");

        execution.setVariable("mandelbrot", "done");
    }

}
