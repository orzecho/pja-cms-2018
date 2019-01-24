import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { VERSION } from 'app/app.constants';
import { Principal, LoginModalService, LoginService } from 'app/core';
import { ProfileService } from '../profiles/profile.service';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.css']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    userIsAdminOrTeacher = true;
    eventSubscriber: Subscription;

    constructor(
        private loginService: LoginService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.profileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
        this.router.events.subscribe(event => this.registerChangeInAuthorisation());
        this.registerChangeInAuthorisation();
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
        this.updateUserAuthorityInfo();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
        this.updateUserAuthorityInfo();
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    private updateUserAuthorityInfo() {
        this.principal
            .hasAnyAuthority(['ROLE_ADMIN', 'ROLE_TEACHER'])
            .then(isAdminOrTeacher => (this.userIsAdminOrTeacher = isAdminOrTeacher));
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    registerChangeInAuthorisation() {
        this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', res => this.updateUserAuthorityInfo());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
}
