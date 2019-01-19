package zetra.iocshark.store.creation;

public class CreationArgs {

        private Object[] args;

        public CreationArgs(Object... args) {
            this.args = args;
        }

        public Object[] getArgs() {
            return this.args;
        }

}
