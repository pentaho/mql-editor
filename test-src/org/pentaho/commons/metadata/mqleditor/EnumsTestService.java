package org.pentaho.commons.metadata.mqleditor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializableException;


       /**
        * RemoteService used to test the use of enums over RPC.
         */
       public interface EnumsTestService extends RemoteService {
           /**
            * Exception thrown when the enumeration state from the client makes it to the
            * server.
            */
          public class EnumStateModificationException extends
                  SerializableException {
               public EnumStateModificationException() {
               }

               public EnumStateModificationException(String msg) {
                    super (msg);
               }
            }

            /**
             * Simplest enum possible; no subtypes or enum constant specific state.
             */
            public enum Basic {
                A, B, C
            }

            /**
             * Enum that has no default constructor and includes state.
             */
            public enum Complex {
                A("X"), B("Y"), C("Z");

                public String value;

                Complex(String value) {
                    this .value = value;
                }

                public String value() {
                    return value;
                }
            }

            /**
             * Enum that has local subtypes.
             */
            public enum Subclassing {
                A {
                    @Override
                    public String value() {
                        return "X";
                    }
                },
                B {
                    @Override
                    public String value() {
                        return "Y";
                    }
                },
                C {
                    @Override
                    public String value() {
                        return "Z";
                    }
                };

                public abstract String value();
            }

            Basic echo(Basic value);

            Complex echo(Complex value) throws EnumStateModificationException;

            Subclassing echo(Subclassing value);
        }
