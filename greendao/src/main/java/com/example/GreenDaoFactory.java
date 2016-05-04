package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoFactory {

    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "com.healthwalk.bean");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itangqi.dao");
        schema.setDefaultJavaPackageDao("com.healthwalk.dao");
        // DaoMaster.java、DaoSession.java、BeanDao.java会放到/java-gen/com/xxx/dao中

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        initBean(schema);

        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "D:\\Users\\Aaron\\AndroidStudioProjects\\HealthGreen\\app\\src\\main\\java-gen");
//        new DaoGenerator().generateAll(schema,args[0]);
    }

    private static void initBean(Schema schema) {
        Entity personBean = schema.addEntity("PersonBean");
        personBean.setTableName("person");
        personBean.addLongProperty("id").primaryKey().index();
//        personBean.addIdProperty().primaryKey().index();
//        personBean.addIntProperty("id").primaryKey().index();
        personBean.addIntProperty("user_id");
        personBean.addStringProperty("date");
        personBean.addStringProperty("time_begin");
        personBean.addStringProperty("time_end");
        personBean.addStringProperty("route");
        personBean.addStringProperty("road");
        personBean.addStringProperty("free");
        personBean.addStringProperty("inhale");
        personBean.addStringProperty("stay");

        Entity carBean = schema.addEntity("CarBean");
        carBean.setTableName("car");
        carBean.addLongProperty("id").primaryKey().index();
//        carBean.addIdProperty().primaryKey().index();
//        carBean.addIntProperty("id").primaryKey().index();
        carBean.addIntProperty("user_id");
        carBean.addStringProperty("date");
        carBean.addStringProperty("time_begin");
        carBean.addStringProperty("time_end");
        carBean.addStringProperty("route");
        carBean.addStringProperty("road");
        carBean.addStringProperty("exhaust");
        carBean.addStringProperty("voc");
        carBean.addStringProperty("co");
        carBean.addStringProperty("pm");
        carBean.addStringProperty("nox");
        carBean.addStringProperty("gasoline");
        carBean.addIntProperty("carkind_id");
        carBean.addIntProperty("drive");
    }

}
