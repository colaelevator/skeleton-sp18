public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static double G=6.67*Math.pow(10,-11);

    public Planet(double xP,double yP,double xV,double yV,double m,String img) {
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }
    public Planet(Planet p) {
        xxPos=p.xxPos;
        yyPos=p.yyPos;
        xxVel=p.xxVel;
        yyVel=p.yyVel;
        mass=p.mass;
        imgFileName=p.imgFileName;
    }

    public double calcDistance(Planet p) {
        return Math.pow(((this.xxPos-p.xxPos)*(this.xxPos-p.xxPos)+(this.yyPos-p.yyPos)*(this.yyPos-p.yyPos)),0.5);
    }
    public double calcForceExertedBy(Planet p) {
        return (this.G*this.mass*p.mass)/(this.calcDistance(p)*this.calcDistance(p));
    }
    public double calcForceExertedByX(Planet p) {
        double dX=-this.xxPos+p.xxPos;
        return this.calcForceExertedBy(p)*dX/this.calcDistance(p);
    }
    public double calcForceExertedByY(Planet p) {
        double dY=-this.yyPos+p.yyPos;
        return this.calcForceExertedBy(p)*dY/this.calcDistance(p);
    }
    private boolean equals(Planet p) {
        return this==p;
    }
    public double calcNetForceExertedByX(Planet[] planets) {
        double sumofNetForcebyX=0.0;
        for(Planet p:planets) {
            if(this.equals(p)) {
                continue;
            } else {
                sumofNetForcebyX+=this.calcForceExertedByX(p);
            }
        }
        return sumofNetForcebyX;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
        double sumofNetForcebyY=0.0;
        for (Planet p:planets) {
            if (this.equals(p)) {
                continue;
            } else {
                sumofNetForcebyY+=this.calcForceExertedByY(p);
            }
        }
        return sumofNetForcebyY;
    }

    public void update(double dt,double Fx,double Fy) {
        double ax=Fx/this.mass;
        double ay=Fy/this.mass;
        this.xxVel+=ax*dt;
        this.yyVel+=ay*dt;
        this.xxPos+=this.xxVel*dt;
        this.yyPos+=this.yyVel*dt;
    }

    public void draw() {
        StdDraw.picture(this.xxPos,this.yyPos,"images/"+this.imgFileName);
    }
}