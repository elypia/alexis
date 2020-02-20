/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.config;

import org.apache.deltaspike.core.api.config.*;

import javax.inject.Singleton;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
@Configuration(prefix = "alexis.api.")
public interface ApiConfig {

    /** The osu! API key. */
    @ConfigProperty(name = "osu")
    String getOsu();

    /** Steam API key. */
    @ConfigProperty(name = "steam")
    String getSteam();

    /** Cleverbot API key. */
    @ConfigProperty(name = "cleverbot")
    String getCleverbot();
}
