public class Main {


    public static void main(String[] args) {

        // input .csp file
        String file = args[0];

        BinaryCSP csp = new BinaryCSPReader().readBinaryCSP(file);

        // reading and processing command line arguments
        // adapted from: http://journals.ecs.soton.ac.uk/java/tutorial/java/cmdLineArgs/parsing.html
        boolean dynamicOrdering = false;
        boolean mac = false;
        int i = 1;
        int j;
        char flag;
        String arg;

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            for (j = 1; j < arg.length(); j++) {
                flag = arg.charAt(j);
                switch (flag) {
                    // mac3 algorithm to be used. If absent fc algorithm is used.
                    case 'm':
                        mac = true;
                        break;
                    // dynamic ordering to be used. If absent static ordering is used.
                    case 'd':
                        dynamicOrdering = true;
                        break;
                    default:
                        System.err.println("ParseCmdLine: illegal option " + flag);
                        break;
                }
            }
        }

        // Initiate solvers.
        if (mac) {
            MaintainArcConsistency macSolver = new MaintainArcConsistency(csp, dynamicOrdering);
            macSolver.solve();
        } else {
            ForwardChecking fcSolver = new ForwardChecking(csp, dynamicOrdering);
            fcSolver.solve();
        }


    }

}
