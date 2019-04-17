package pku.lesson_evaluator;

public class Class_item {
    private String className;
    private String classTeacher;
    private String classScore;

    public Class_item(String className,String classTeacher,String classScore){
        this.className=className;
        this.classTeacher=classTeacher;
        this.classScore=classScore;
    }

    public String getClassName() {
        return className;
    }

    public String getClassTeacher() {
        return classTeacher;
    }

    public String getClassScore() {
        return classScore;
    }
}
