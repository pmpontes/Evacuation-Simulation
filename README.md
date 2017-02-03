#Evacuation Simulation

##Summary

Developed for a Distributed Artificial Intelligence and Agents course, this project allows simulating the interaction of confined agents in a concrete space when the it becomes necessary to evacuate that space.

##A non-deterministic multi-agent approach for emergency evacuation simulation

An emergency evacuation implies moving a group of people away from a life-threatening hazard. The efficiency with which the population evacuates is crucial for minimizing the number of fatalities and injuries. Evacuating a population is not, however, a straightforward process. Different people behave diversly in face of a threat, thus knowing how these behaviours affect the overall operation is necessary for defining an efficient evacuation plan. Individual psychological and physical attributes, such as area knowledge, altruism, mobility, panic, patience and dependency, influence the behaviour and may vary over time.

Techniques for analyzing the evacuation of an environment include fluid mechanics and automata. These approaches, however, fail to take into account the human nature of the evacuees. Therefore, in this project, a proof of concept was developed which simulates an emergency evacuation, making use of a multi-agent system model.

This approach allows the definition of various possible behaviours for the individuals in the population, namely: screaming, requesting help, helping, requesting directions, providing directions and moving. Each of these behaviours is influenced by the attributes mentioned earlier, while also afecting these same attributes for themselves or the people around them.

With this proof of concept, it is possible to observe the evacuation process and the variations on the average of each attribute of the entire population in real time, allowing the user to analyze how the behaviours affect the overall evacuation time and the fatality count. The user can specify the configuration of the scenario, where the values for each individual's attributes can be defined. It is also possible to specify the environment, through the configuration of the number of exits and obstacles, as well as their respective location.

##Getting Started

###Pre-requisites

JADE: Definition of agents.

Repast Simphony: Multi-agent simulation.

SAJaS: Integration of JADE agents with Repast.

###Usage
####Drawing the map
In order to facilitate the design of a map, a tool Simple to do so, EnvironmentBuilder.
This tool, however, is primitive and its purpose consisted merely in helping to create maps for tests.

####Specifying the map
The field that specifies the simulation space should have as content a set of lines, all of the same length, composed of characters
'W' - which represents a wall, or, more generally, an obstacle -, 'E' - which represents an output - e '', which characterizes a free space. The order of characters in the lines, and the lines in the file, is the order considered for the representation of the map in the simulation environment.

The file can be selected in the Repast GUI through the Parameters tab in the sidebar, by entering the address for the file in the box labeled "Environment File Name".

####Specifying the scenario
The file specifying the population to be used in the simulation is a .xml file, and allows to configure both atribures pertaining to the enrire simulation as well as the attributes for each individual to be evacuated:

```xml
<scenario
  panicVariation="INT"
  mobilityVariation="INT"
  knowledgeAcquisitionFactor="DOUBLE"
  patienceVariation="INT"
  patienceThreshold="INT"
>
  <person>
    <position x="INT"y="INT"/>
    <areaKnowledge>INT</areaKnowledge> <!--required-->
    <independence>INT</independence> <!--required-->
    <altruism>INT</altruism>
    <patienceVariation>INT</patienceVariation>
    <mobility>INT</mobility>
    <panic>INT</panic>
    <age>INT</age>
  </person>
</scenario>
```

The file can be selected in the Repast GUI through the Parameters tab in the sidebar, by entering the address for the file in the box labeled "Scenario File Name".
