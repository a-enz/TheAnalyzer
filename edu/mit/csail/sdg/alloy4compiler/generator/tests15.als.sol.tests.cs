// This C# file is generated from ..\edu\mit\csail\sdg\alloy4compiler\generator\tests15.als

using System;
using System.Linq;
using System.Collections.Generic;
using System.Diagnostics.Contracts;

public static class Test {
  public static void Main(string[] args) {
    // setup test data
    var ManSet = new HashSet<Man>();
    ManSet.Add(new Man());

    // check test data
    Contract.Assert(FuncClass.Id(ManSet).Count() == 1, "Assertion");
  }
}
