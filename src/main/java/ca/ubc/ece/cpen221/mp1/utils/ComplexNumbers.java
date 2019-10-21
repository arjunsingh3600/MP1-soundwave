package ca.ubc.ece.cpen221.mp1.utils;

public class ComplexNumbers {

    double real,img;

    public ComplexNumbers(double real, double img){
        this.real = real;
        this.img = img;
    }

    public ComplexNumbers sum(ComplexNumbers z){
        double a = real + z.real;
        double b = img + z.img;
        return new ComplexNumbers(a,b);
    }

    public ComplexNumbers multiply(ComplexNumbers z){
        double a = real*z.real - img*z.img;
        double b = real*z.img + img*z.real;
        return new ComplexNumbers(a,b);
    }

    public ComplexNumbers multiply(double c){
        double a = real*c;
        double b = img*c;

        return new ComplexNumbers(a,b);
    }

    public double magnitude(){

        double mag = Math.pow( Math.pow(real,2) + Math.pow(img,2), 0.5);

        return mag;
    }

    public void conjugate(){
        img *= -1;
    }

    public String toString(){
        return real + " + " + img + "i";
    }

}
