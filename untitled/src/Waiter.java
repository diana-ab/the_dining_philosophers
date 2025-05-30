import java.util.ArrayList;
import java.util.List;

public class Waiter {
    private List <Philosopher> setingPhilosopher;
    private int maxSetingPhilosophers;

    public Waiter(int cherNumber) {
        this.maxSetingPhilosophers =cherNumber-1;
        this.setingPhilosopher=new ArrayList<>();

    }
//    הצעה שלי היא שהמלצר הוא זה שנותן רשות לקחת מזלג ומזלג לפילו אבל גם הוא שומר במקביל להכי מורעב בזמן נתון את המזלגות
//    שככה שבזמן שהוא מחכה בין לקיחת מזלג למזלג הם לא ילקחו לו
//    השינוי יהיה שכלל הפילוסופים יבקשו כל פעם לדעת מהמלצר עם המזלג שלהם פנוי
//    כל פעם מזלג אחר בתורו


//    הצעה שלי היא שהמלצר הוא זה שנותן רשות לקחת מזלג ומזלג לפילו אבל גם הוא שומר במקביל להכי מורעב בזמן נתון את המזלגות
//    שככה שבזמן שהוא מחכה בין לקיחת מזלג למזלג הם לא ילקחו לו
//    השינוי יהיה שכלל הפילוסופים יבקשו כל פעם לדעת מהמלצר עם המזלג שלהם פנוי
//    כל פעם מזלג אחר בתורו
//
//
//    ChatGPT said:
//    רעיון מצוין ואלגנטי!
//    שיטת המלצר שלך מאפשרת פתרון מבוקר ושקוף למזלגות, תוך מניעת דדלוק וגם שליטה טובה על מי מחזיק מה ומתי.
//
//    איך לממש את זה בקוד?
//    רעיון עיקרי
//    המלצר שולט במזלגות ומקצה אותם לפילוסופים לפי תור.
//
//    כל פילוסוף לא "תופס" מזלג ישירות, אלא מבקש מהמלצר רשות לקחת מזלג אחד בכל פעם.
//
//    המלצר בודק לפי סדר העדיפות (מי הכי מורעב וכו'), ומאפשר רק לפילוסוף אחד להחזיק מזלג מסוים בכל רגע.
//
//            המלצר שומר "בזמן אמת" את המזלגות של הפילוסוף הכי מורעב כדי שלא יפסיד אותם (למשל בין ניסיון לקחת מזלג ראשון למזלג שני).
//
//    כך אין סיכון שמזלג יילקח ע"י פילוסוף אחר בזמן הפסקה בין שני המזלגות.





    public synchronized void requestSeat(Philosopher p) throws InterruptedException {
        while (this.setingPhilosopher.size() >= this.maxSetingPhilosophers) {
            wait();
        }
        this.setingPhilosopher.add(p);
        this.notifyAll();
    }

    public synchronized void leaveSeat(Philosopher p) {
        this.setingPhilosopher.remove(p);
        this.notifyAll();
    }







    private boolean ifLeftForkAvailable(Philosopher philosopher){
        Fork leftFork1=philosopher.getLeftFork1();
        if (leftFork1.getHeldBy()!=null){
            return false;
        }
        return true;
    }
    private boolean ifRightForkAvailable(Philosopher philosopher){
        Fork rightFork2=philosopher.getRightFork2();
        if (rightFork2.getHeldBy()!=null){
            return false;
        }
        return true;
    }

    private Philosopher findTheStarvingPhilosopher(){
        int minEatingCount= this.setingPhilosopher.get(0).getEatingCount();
        Philosopher starvingPhilosopher = this.setingPhilosopher.get(0);
        for (Philosopher philosopher : this.setingPhilosopher){
            if(philosopher.getEatingCount()<minEatingCount){
                minEatingCount=philosopher.getEatingCount();
                starvingPhilosopher=philosopher;
            }
        }
        return starvingPhilosopher;
    }



}
