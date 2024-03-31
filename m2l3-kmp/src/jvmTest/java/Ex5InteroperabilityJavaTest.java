import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.otuskotlin.kmp.kt2java.InteroperabilityJava;
import ru.otus.otuskotlin.kmp.kt2java.MyClass;
import ru.otus.otuskotlin.kmp.kt2java.UtilKt;
import ru.otus.otuskotlin.kmp.kt2java.Utils;

import java.io.SyncFailedException;

public class Ex5InteroperabilityJavaTest {

    /**
     * Демонстрация доступа к компаньонам
     */
    @Test
    void companionMethods() {
        // Companion
        InteroperabilityJava.Companion.functionOne();
    }

    /**
     * Демонстрация доступа к компаньонам
     */
    @Test
    void companionMethod2() {
        // Companion
        InteroperabilityJava.functionOne();
    }

    /**
     * Работа с методами с дефолтными аргументами.
     * Требуется @JvmOverloads
     */
    @Test
    void defaultArguments() {
        System.out.println(
                new InteroperabilityJava().defaults()
        );
        System.out.println(new InteroperabilityJava().defaults("p1"));
        System.out.println(
                new InteroperabilityJava().defaults("123", 123, true)
        );
    }

    /**
     * Явное указание наименования в Java через @JvmName
     */
    @Test
    void jvmName() {
        System.out.println(new InteroperabilityJava().asdAsd());
    }

    /**
     * Доступ к глобальным функциям (которые вне классов)
     * Обратите внимание!!!
     * В котлине это два файла.
     * Но за счет JvmMultifileClass и @file:JvmName мы получаем к ним доступ через Utils
     */
    @Test
    void globalFunctions() {
        Assertions.assertEquals("date", Utils.getDate());
        Assertions.assertEquals("name", Utils.getName());
        UtilKt.getDate1();
    }

    @Test
    void properties() {
        // Дефлотные значения
        MyClass mc = new MyClass();
        Assertions.assertEquals("a-prop", mc.a); // доступ только по backing field
        Assertions.assertEquals("b-prop", mc.getB()); // доступ только по геттеру
        Assertions.assertEquals("c-prop", mc.getC()); // доступ только по геттеру

        // Кастомные значения
        MyClass mcc = new MyClass("a-field", "b-field", "c-field");
        // доступ по сеттеру
        mcc.setC("c-changed");

        Assertions.assertEquals("a-field", mcc.a); // доступ только по backing field
        Assertions.assertEquals("b-field", mcc.getB()); // доступ только по геттеру
        Assertions.assertEquals("c-changed", mcc.getC()); // доступ только по геттеру
    }

    @Test
    void syncFun() {
        MyClass mc = new MyClass();
        // Без try-catch работать не будет из-за объявленного checked exception
        try {
            mc.syncFun();
            mc.funWithSync();
        } catch (SyncFailedException e) {
            System.out.println("Exception???");
        }
    }
}
