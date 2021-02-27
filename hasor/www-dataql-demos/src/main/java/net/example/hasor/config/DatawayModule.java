/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.example.hasor.config;
import net.example.hasor.auth.AuthAuthorizationChainSpi;
import net.example.hasor.auth.AuthLoginPerformChainSpi;
import net.example.hasor.auth.AuthLoginTokenChainSpi;
import net.hasor.core.DimModule;
import net.hasor.dataql.Finder;
import net.hasor.dataql.QueryApiBinder;
import net.hasor.dataway.dal.providers.db.InformationStorage;
import net.hasor.dataway.spi.AuthorizationChainSpi;
import net.hasor.dataway.spi.LoginPerformChainSpi;
import net.hasor.dataway.spi.LoginTokenChainSpi;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import net.hasor.web.WebApiBinder;
import net.hasor.web.WebModule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2021-01-02
 */
@DimModule
@Component
public class DatawayModule implements WebModule, SpringModule {
    @Resource(name = "metadataDs")
    private DataSource metadataDs = null;
    @Resource(name = "dataDs1")
    private DataSource dataDs1    = null;
    @Resource(name = "dataDs2")
    private DataSource dataDs2    = null;

    @Override
    public void loadModule(WebApiBinder apiBinder) throws Throwable {
        apiBinder.setEncodingCharacter("UTF-8", "UTF-8");
        apiBinder.loadMappingTo(EchoController.class);
        //
        // .check dataSource
        Objects.requireNonNull(this.metadataDs, "metadataDs is null");
        Objects.requireNonNull(this.dataDs1, "dataDs1 is null");
        Objects.requireNonNull(this.dataDs2, "dataDs2 is null");
        //
        // .isolation meta-tables using InformationStorage
        apiBinder.bindType(InformationStorage.class).toInstance(() -> {
            return this.metadataDs;
        });
        //
        // .add two dataSources into Dataway
        apiBinder.installModule(new JdbcModule(Level.Full, "ds1", this.dataDs1));
        apiBinder.installModule(new JdbcModule(Level.Full, "ds2", this.dataDs2));
        //
        // udf/udfSource/import 指令 的类型创建委托给 spring
        QueryApiBinder queryBinder = apiBinder.tryCast(QueryApiBinder.class);
        queryBinder.bindFinder(Finder.TYPE_SUPPLIER.apply(springTypeSupplier(apiBinder)));
        //
        // spi`s
        apiBinder.bindSpiListener(LoginPerformChainSpi.class, AuthLoginPerformChainSpi.class);
        apiBinder.bindSpiListener(LoginTokenChainSpi.class, AuthLoginTokenChainSpi.class);
        apiBinder.bindSpiListener(AuthorizationChainSpi.class, AuthAuthorizationChainSpi.class);
    }
}
