<apiTag>
    <section class="section">
        <basicTag></basicTag>
        <parameterTableTag></parameterTableTag>
        <div class="columns is-multiline" if="{null!=api}">
            <div class="column is-12">
                <h1 class="title">
                    <span>操作</span>
                </h1>
            </div>
            <div class="column is-12">
                <div class="tabs is-toggle">
                    <ul>
                        <li class="{'is-active':tabName=='execute'}" data-tab="execute" onclick="{changeTab}"><a>执行接口</a></li>
                        <li class="{'is-active':tabName=='parameterEntity'}" data-tab="parameterEntity" onclick="{changeTab}"><a>请求参数实体类信息</a></li>
                        <li class="{'is-active':tabName=='returnEntity'}" data-tab="returnEntity" onclick="{changeTab}"><a>返回结果实体类信息</a></li>
                        <li class="{'is-active':tabName=='apiInfo'}" data-tab="apiInfo" onclick="{changeTab}"><a>API信息</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <executeTag show="{tabName=='execute'}"></executeTag>
        <parameterEntityTag show="{tabName=='parameterEntity'}"></parameterEntityTag>
        <returnEntityTag show="{tabName=='returnEntity'}"></returnEntityTag>
        <apiInfoTag show="{tabName=='apiInfo'}"></apiInfoTag>
    </section>
    <script>
        let self = this;
        this.on("mount",function(){
            riot.eventBus.on('showApi', function(api){
                self.api = api;
                self.tabName = "execute";
                window.scrollTo(0,0);
                self.update();
            });
        });
        self.changeTab = function(event){
            self.tabName = event.currentTarget.dataset.tab;
        };
    </script>
</apiTag>