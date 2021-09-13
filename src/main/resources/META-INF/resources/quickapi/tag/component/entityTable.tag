<entityTableTag>
    <div class="column is-12">
        <h1 class="title">{opts.entity.simpleName}{opts.entity.description}</h1>
        <table class="table is-fullwidth is-large">
            <thead>
            <tr>
                <th>字段名</th>
                <th>字段类型</th>
                <th>描述</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><b>{opts.entity.simpleName}</b></td>
                <td><b>{opts.entity.className}</b></td>
                <td><b>{opts.entity.description}</b></td>
            </tr>
            <tr each="{apiField in opts.entity.apiFields}">
                <td>&nbsp;&nbsp;{apiField.name}</td>
                <td>{apiField.className}</td>
                <td>{apiField.description}</td>
            </tr>
            </tbody>
        </table>
    </div>
</entityTableTag>