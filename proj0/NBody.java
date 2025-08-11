public class NBody {
    private static int N;
    public static double readRadius(String FileName) {
        In in=new In(FileName);
        N=in.readInt();
        return in.readDouble();
    }
    public static Planet[] readPlanets(String FileName) {
        In in=new In(FileName);
        int N=in.readInt();
        in.readDouble();
        Planet[] planets=new Planet[N];
        for (int i=0;i<N;i++) {
            double xxPos=in.readDouble();
            double yyPos=in.readDouble();
            double xxVel=in.readDouble();
            double yyVel=in.readDouble();
            double mass=in.readDouble();
            String img=in.readString();
            planets[i]=new Planet(xxPos,yyPos,xxVel,yyVel,mass,img);
        }
        return planets;
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        double T;
        double dt;
        double radius;
        double time;
        Planet[] planets;
        String filename;
        T=Double.parseDouble(args[0].trim());
        dt=Double.parseDouble(args[1].trim());
        filename=args[2];
        radius=NBody.readRadius(filename);
        planets=NBody.readPlanets(filename);
        StdDraw.setScale(-radius,radius);
        StdDraw.clear();

        time=0;
        while (time!=T) {
            double[] xForces=new double[N];
            double[] yForces=new double[N];
            for (int i=0;i<N;i++) {
                xForces[i]=planets[i].calcNetForceExertedByX(planets);
                yForces[i]=planets[i].calcNetForceExertedByY(planets);
            }
            for (int i=0;i<N;i++) {
                planets[i].update(dt,xForces[i],yForces[i]);
            }
            StdDraw.picture(0,0,"images/starfield.jpg");
            for (int i=0;i<planets.length;i++) {
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time+=dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
        }
    }