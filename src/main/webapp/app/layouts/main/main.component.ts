import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css']
})
export class JhiMainComponent implements OnInit {
    constructor(public router: Router) {}

    ngOnInit() {}
}
