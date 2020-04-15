package org.spring.util;

import com.ww.exception.WWException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeanFactory {
    Map<String, Object> map = new HashMap<>();

    public BeanFactory(String xml) {
        parseXml(xml);
    }

    public void parseXml(String xml) throws WWException{
        File file = new File(this.getClass().getResource("/").getPath() + "//" + xml);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            //实例化对象
            Element rootElement = document.getRootElement();
            Attribute attribute = rootElement.attribute("default-autowire");
            boolean flag = false;
            if (attribute != null) {
                flag = true;
            }
            for (Iterator<Element> it = rootElement.elementIterator(); it.hasNext(); ) {
                Element firstElement = it.next();
                Attribute id = firstElement.attribute("id");
                String beanName = id.getValue();
                Attribute aClass = firstElement.attribute("class");
                String clazzName = aClass.getValue();
                Class clazz = Class.forName(clazzName);
                Object object = null;
                /**
                 * 维护依赖关系
                 * 看是否有依赖（判断是否有property，或是否有属性，有则注入）
                 */
                for (Iterator<Element> sec = firstElement.elementIterator(); sec.hasNext(); ) {
                    //得到ref的value，通过value得到对象（从map里）
                    //得到name的值，根据值获取一个field对象
                    //通过field的set方法set那个对象
                    //<proterty name="dao" ref="dao"/>
                    Element secElementChild = sec.next();
                    if (secElementChild.getName().equals("property")) {
                        //ref属性对应的值作为map的key去取出对象，也就是依赖的那个对象的class
                        //调用默认的构造函数创建对象，setter方式注入，没有特殊构造方法
                        object = clazz.newInstance();
                        String refValue = secElementChild.attribute("ref").getValue();
                        //实现类
                        Object injectObject = map.get(refValue);
                        String nameValue = secElementChild.attribute("name").getValue();
                        //获得声明的属性
                        Field field = clazz.getDeclaredField(nameValue);
                        //属性为私有的
                        field.setAccessible(true);
                        //给属性设值，
                        field.set(object, injectObject);
                    } else {
                        //特殊构造方法方式注入（自己传参的构造函数，非默认）
                        //构造方法引用的值
                        String refValue = secElementChild.attribute("ref").getValue();
                        //要注入的对象（接口的实现类）
                        Object injectObject = map.get(refValue);
                        //要注入的类
                        Class injectClazz = injectObject.getClass();
                        //要注入的类的接口，获取使用依赖类的构造方法，将依赖的接口传入构造方法
                        Constructor constructor = clazz.getConstructor(injectClazz.getInterfaces()[0]);
                        object = constructor.newInstance(injectObject);
                    }
                }
                //如果是自动注入
                if (flag) {
                    if (attribute.getValue().equals("byType")) {
                        //判断是否有依赖
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            //得到field属性的类型，比如String a,类型就是String.class
                            //注入对象的类型
                            Class injectObjectClazz = field.getType();
                            /**
                             * byType，遍历map中的所有对象，判断对象类型是否喝injectObjectClazz相同
                             */
                            int count = 0;
                            Object injectObject = null;
                            for (String key : map.keySet()) {
                                Class temp = map.get(key).getClass().getInterfaces()[0];
                                if (temp.getName().equals(injectObjectClazz.getName())) {
                                    injectObject = map.get(key);
                                    //记录找到一个，因为可能找到多个
                                    count++;
                                }
                            }
                            if (count > 1) {
                                throw new WWException("需要一个对象，但找到了两个");
                            }else{
                                //byType时
                                object=clazz.newInstance();
                                field.setAccessible(true);
                                field.set(object,injectObject);
                            }
                        }
                    }
                }
                //没有子标签
                if (object == null) {
                    object = clazz.newInstance();
                }
                map.put(beanName, object);
            }
        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }

    public Object getBean(String beanName) {
        return map.get(beanName);
    }
}
