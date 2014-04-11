package edu.mit.csail.sdg.alloy4compiler.generator;

import java.util.List;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprBinary;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprUnary;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.VisitQuery;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;

public class PostconditionVisitor extends VisitQuery<Object>{

	//dummy for ExrpBinary visit. does not distinguish the operator so far 
    @Override public String visit(ExprBinary x) throws Err {
    	switch(x.op) {
    	case ARROW:
    	case ANY_ARROW_SOME:
    	case ANY_ARROW_ONE:
    	case ANY_ARROW_LONE:
    	case SOME_ARROW_ANY:
    	case SOME_ARROW_SOME:
    	case SOME_ARROW_ONE:
    	case SOME_ARROW_LONE:
    	case ONE_ARROW_ANY:
    	case ONE_ARROW_SOME:
    	case ONE_ARROW_ONE:
    	case ONE_ARROW_LONE:
    	case LONE_ARROW_ANY:
    	case LONE_ARROW_SOME:
    	case LONE_ARROW_ONE:
    	case LONE_ARROW_LONE:
    		return ("set of");
    	case JOIN:
    		return (x.left.accept(this) + "." + x.right.accept(this));
    	case INTERSECT:
    		return ("((" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "));");
    	case PLUS:
    		return ("((" + x.left.accept(this) + ").Union(" + x.right.accept(this) + "))");
    	case IPLUS:
    		return ("(" + x.left.accept(this) + " + " + x.right.accept(this) + ")");
    	case MINUS:
    		return ("((" + x.left.accept(this) + ").Except(" + x.right.accept(this) + "))");
    	case IMINUS:
    		return ("(" + x.left.accept(this) + " - " + x.right.accept(this) + ")");
    	case MUL:
    		return ("(" + x.left.accept(this) + " * " + x.right.accept(this) + ")");
    	case DIV:
    		return ("(" + x.left.accept(this) + " / " + x.right.accept(this) + ")");
    	case REM:
    		return ("(" + x.left.accept(this) + " % " + x.right.accept(this) + ")");
    	case EQUALS:
    		return ("((" + x.left.accept(this) + ").Equals((" + x.right.accept(this) + ")))");
    	case NOT_EQUALS:
       		return ("(!(" + x.left.accept(this) + ").Equals((" + x.right.accept(this) + ")))");	
    	case IMPLIES:
       		return ("((" + x.left.accept(this) + ") -: (" + x.right.accept(this) + "))");	
    	case LT:
    		return ("(" + x.left.accept(this) + " < " + x.right.accept(this) + ")");
    	case LTE:
    		return ("(" + x.left.accept(this) + " <= " + x.right.accept(this) + ")");
    	case GT:
    		return ("(" + x.left.accept(this) + " > " + x.right.accept(this) + ")");
    	case GTE:
    		return ("(" + x.left.accept(this) + " >= " + x.right.accept(this) + ")");
    	case NOT_LT:
    		return ("(!(" + x.left.accept(this) + " < " + x.right.accept(this) + "))");
    	case NOT_LTE:
    		return ("(!(" + x.left.accept(this) + " <= " + x.right.accept(this) + "))");
    	case NOT_GT:
    		return ("(!(" + x.left.accept(this) + " > " + x.right.accept(this) + "))");
    	case NOT_GTE:
    		return ("(!(" + x.left.accept(this) + " >= " + x.right.accept(this) + "))");
    	case IN:
    		return ("((" + x.right.accept(this) + ").Contains(" + x.right.accept(this) + "))");
    	case NOT_IN:
    		return ("(!(" + x.right.accept(this) + ").Contains(" + x.right.accept(this) + "))");
    	case AND:
    		return ("(" + x.left.accept(this) + " && " + x.right.accept(this) + ")");
    	case OR:
    		return ("(" + x.left.accept(this) + " || " + x.right.accept(this) + ")");
    	case IFF:
    		return ("(" + x.left.accept(this) + " == " + x.right.accept(this) + ")");
    	default:
    		return "noScpecifiedBinaryOperation";
    	}
    }
    
    
    

    /** Visits a Sig node (this default implementation simply returns null) */
    @Override public String visit(Sig x) throws Err {
    	//just return the label
    	if(x.label == "Int") return "int";
    	else if(x.label.startsWith("boolean/")) return "bool";
    	else return x.label.substring(5);

    }

    
    public String visit(ExprVar f) throws Err{
    	return null;
    }
    
    public String visit(Field x) throws Err {
        return null;
    }
    
    
    
    public String visit(ExprUnary x) throws Err {
    	switch (x.op){
    		case SOMEOF: return "some of";
            case LONEOF: return "lone of";
            case ONEOF: return "one of";
            case SETOF: return "set of";
            //TODO this operators must be handled
    		case CLOSURE: break;
            case RCLOSURE: break;
    		case NOOP: break;
            case NOT: break;
            case TRANSPOSE: break;
            case CARDINALITY: break;
            case CAST2INT:  break;
            case CAST2SIGINT: break;
    		default: break;
    	}
    	return x.op.toString();
    }

}
