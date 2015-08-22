$fn = 10;

include <9g_servo.scad>
include <servo_arm.scad>
include <springs.scad>

module picker_motor(angle) {
    rotate(angle, [0, 1, 0]) union() {
        9g_motor(); 
        // motor support
        translate([14, 0, -11]) cylinder(h=15, r=2.5);
        translate([-14, 0, -11]) cylinder(h=15, r=2.5);
    }
}

module picker_dispenser(height=150, width=50, depth=80, r=0) {
    half_width = width / 2;
    pos = height-half_width;
    // dispenser door motor
    translate([half_width+5.5, 0, -10]) picker_motor(180);
    // dispenser rod
    translate([half_width, 0, -height-4]) color("Silver") cylinder(h=height-24, r=1);
    // dispenser rod stabilizer
    translate([half_width, 0, -pos]) difference() {
        union() {
            cylinder(h=6, r=5);
            difference() {
                translate([0, 0, 2]) cylinder(h=2, r=half_width-1);
                translate([-half_width+1, -half_width, 1.9]) cube([width, half_width, 2.2]);
            }
        };
        translate([0, 0, -0.1]) cylinder(h=6.2, r=1.3);
    };
    // bottom door (half moon)
    translate([half_width, 0, -height-5]) rotate(r, [0, 0, 1]) color("Red") difference() {
        union() {
            cylinder(h=4, r=5);
            difference() {
                translate([0, 0, 2.4]) cylinder(h=2, r=half_width);
                translate([-half_width, -half_width, 2.3]) cube([width, half_width, 2.2]);
            }
        };
        translate([0, 0, -0.1]) cylinder(h=4.2, r=1.3);
    };
}

module picker_tube_track(d=2) {
    // track
    union() {
        difference() {
            cube([7, 50, 10]);
            translate([2, -0.1, 3]) cube([5.1, 50.2, 4]);
            translate([3, -0.1, 2]) cube([2, 50.2, 6]);
        };
        // rod vertical stabilizer
        translate([2, d, 2]) difference() {
            union() {
                translate([0, 0, 1]) cube([10, 6, 4]);
                translate([2, 0, 0]) cube([2, 6, 6]);
            };
            translate([7, 3, 0.9]) cylinder(h=4.2, r=1.2);
        };
    };
};

module picker_tube(height=100,offset=20) {
    // picker tube
    translate([0, 0, offset]) union() {
        difference() {
            union() {
                // tube
                cylinder(h=height, r=1.2);
                // picker stopper
                translate([0, -0.5, 59-offset]) cylinder(h=3, r=3);
                // pill stabilizer
                translate([0, -0.5, 0]) cylinder(h=.2, r=3);
            };
            translate([0, 0, -0.1]) cylinder(h=height+0.2, r=1);
        };
        // spring
        color("Black") scale([1, 1, 1]) spring(Windings=10, R=3, r=0.1, h=55-offset);
    };
};

module picker_set(r=180,offset=20,height=100) {
    // picker motor
    picker_motor(-90);
    // picker motor arm
    translate([-17, 0, 5.5]) rotate(r, [1, 0, 0]) union() {
        rotate(-90, [0, 1, 0]) servo_standard(20, 1);
        translate([-9, 20, -3]) difference() {
            translate([-3, 0, 3]) rotate(90, [0, 1, 0]) cylinder(h=6, r=3);
        };
    };
    translate([-26, 20 * cos(r), -50 + 20 * sin(r)]) {
        picker_tube(height,offset);
    }
    translate([-35, -26, 35]) picker_tube_track(d=22+20*cos(r));
}

module picker_case(height=150, width=50, depth=80, base_height=10, base_offset = 0, thickness=2) {
    
    half_width = width / 2;
    difference() {
        union() {
            // external walls
            difference() {
                union() {
                    cube([width, depth, height]);
                    translate([half_width, depth]) cylinder(h=height, r=half_width);
                };
                translate([thickness, -0.1, -0.1]) cube([width - thickness*2, depth+0.3, height + 0.2]);
                translate([half_width, depth, -0.1]) cylinder(h=height+0.2, r=half_width-thickness);
            };
            // base ramp
            difference() {
                translate([half_width, base_offset, -0.2]) {
                    rotate(90, [0, 0, 1]) {
                        polyhedron(
                            points = [
                                [0, -half_width+thickness, base_height],
                                [0, half_width-thickness, base_height],
                                [0, half_width-thickness, 0],
                                [0, -half_width+thickness, 0],
                                [depth-base_offset, -half_width+thickness, 0],
                                [depth-base_offset, half_width-thickness, 0]
                            ],
                            faces = [
                                [0, 3, 2],
                                [0, 2, 1],
                                [3, 0, 4],
                                [1, 2, 5],
                                [0, 5, 4],
                                [0, 1, 5],
                                [5, 2, 4],
                                [4, 2, 3]
                            ]
                        );
                    };
                };
                // rod hole
                translate([half_width, depth, -0.3]) cylinder(h=2, r=3);
            };
        };
    };
    // top lid
    translate([0, 0, height]) difference() {
        union() {
            cube([width, depth, 2]);
            translate([half_width, depth]) cylinder(h=2, r=half_width);
        };
//        translate([10, -0.1, -0.1]) cube([3, 30, 2.2]);
    };
};


r = $t < 0.5? 30+360 * $t : 360 - 300 * $t;

// main body
module picker(height=150, width=50, depth=80) {
    translate([0, 0, 0]) rotate(0) union() {
        picker_case(height, width, depth); 
        translate([0, depth, height]) picker_dispenser(height, width, depth, r=360*$t);
        translate([width-13, 6, 50]) picker_set(r,offset=10);
    };
};

picker();