package org.sat4j.pb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

public class BugSAT117 {

    private ISolver solver;

    @Before
    public void setUp() throws ContradictionException {
        solver = SolverFactory.newEclipseP2().getSolvingEngine();
        IVecInt clause = new VecInt();
        clause.push(1).push(2).push(3);
        solver.addClause(clause);
    }

    @Test
    public void testTimeoutOnConflict() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        solver.setTimeoutOnConflicts(100);
        assertTrue(solver.isSatisfiable());
        Field field = solver.getClass().getSuperclass().getSuperclass()
                .getDeclaredField("timer");
        field.setAccessible(true);
        assertNull(field.get(solver));
    }

    @Test
    public void testTimeoutOnConflictGlobal() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        solver.setTimeoutOnConflicts(100);
        assertTrue(solver.isSatisfiable(true));
        Field field = solver.getClass().getSuperclass().getSuperclass()
                .getDeclaredField("timer");
        field.setAccessible(true);
        assertNull(field.get(solver));
    }

    @Test
    public void testTimeoutSeconds() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        solver.setTimeout(10);
        assertTrue(solver.isSatisfiable());
        Field field = solver.getClass().getSuperclass().getSuperclass()
                .getDeclaredField("timer");
        field.setAccessible(true);
        assertNull(field.get(solver));
    }

    @Test
    public void testTimeoutSecondsGlobal() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        solver.setTimeout(10);
        assertTrue(solver.isSatisfiable(true));
        Field field = solver.getClass().getSuperclass().getSuperclass()
                .getDeclaredField("timer");
        field.setAccessible(true);
        assertNotNull(field.get(solver));
    }

    @Test
    public void testTimeoutSecondsLoop() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        for (int i = 0; i < 1000; i++) {
            solver = SolverFactory.newEclipseP2();
            IVecInt clause = new VecInt();
            clause.push(1).push(2).push(3);
            solver.addClause(clause);
            solver.setTimeout(10);
            assertTrue(solver.isSatisfiable());
            Field field = solver.getSolvingEngine().getClass().getSuperclass()
                    .getSuperclass().getDeclaredField("timer");
            field.setAccessible(true);
            assertNull(field.get(solver.getSolvingEngine()));
        }
    }

    @Test
    public void testTimeoutConflictsLoop() throws ContradictionException,
            TimeoutException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        for (int i = 0; i < 1000; i++) {
            solver = SolverFactory.newEclipseP2();
            IVecInt clause = new VecInt();
            clause.push(1).push(2).push(3);
            solver.addClause(clause);
            solver.setTimeoutOnConflicts(10);
            assertTrue(solver.isSatisfiable());
            Field field = solver.getSolvingEngine().getClass().getSuperclass()
                    .getSuperclass().getDeclaredField("timer");
            field.setAccessible(true);
            assertNull(field.get(solver.getSolvingEngine()));
        }
    }
}
