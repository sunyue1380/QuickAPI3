<settingsTag>
    <section class="section">
        <div class="container">
            <h1 class="title">全局头部</h1>
            <h2 class="subtitle">
                设置头部字段,这些头部字段将会在执行请求时添加到请求头.
            </h2>
            <table class="table is-striped is-narrow is-hoverable is-fullwidth">
                <thead>
                    <th>头部名称</th>
                    <th>头部值</th>
                    <th>备注</th>
                    <th>操作</th>
                </thead>
                <tr each="{header in settings.headers}">
                    <td>{header.key}</td>
                    <td>{header.value}</td>
                    <td>{header.description}</td>
                    <td><button class="button is-danger" onclick="{removeHeader}">删除</button></td>
                </tr>
                <tfoot>
                    <td><input ref="headerKey" class="input" type="text" placeholder="头部名称"/></td>
                    <td><input ref="headerValue" class="input" type="text" placeholder="头部值"/></td>
                    <td><input ref="headerDescription" class="input" type="text" placeholder="备注"/></td>
                    <td><button class="button is-primary" onclick="{addHeader}">新增</button></td>
                </tfoot>
            </table>
        </div>
        <script>
            let self = this;
            self.mixin("commonService");
            self.settings = this.getSettings();

            /**添加全局头部*/
            self.addHeader = function(){
                let header = {
                    "key": self.refs.headerKey.value,
                    "value": self.refs.headerValue.value,
                    "description": self.refs.headerDescription.value,
                };
                if(null==header.key||""==header.key){
                    alert("头部key值不能为空!");
                    return;
                }
                for(let i=0;i<self.settings.headers.length;i++){
                    if(self.settings.headers[i].key==[header.key]){
                        if(confirm("该头部key已存在,确认覆盖该头部吗?")){
                            self.settings.headers[i].value = header.value;
                            self.settings.headers[i].description = header.description;
                            this.saveSettings(self.settings);
                        }
                        return;
                    }
                }
                self.settings.headers.push(header);
                this.saveSettings(self.settings);
            };

            /**移除全局头部*/
            self.removeHeader = function(){
                if(confirm("确认删除该头部吗?")){
                    for(let i=0;i<self.settings.headers.length;i++){
                        if(self.settings.headers[i].key==[this.header.key]){
                            self.settings.headers.splice(i,1);
                        }
                    }
                    this.saveSettings(self.settings);
                }
            };
        </script>
    </section>
</settingsTag>