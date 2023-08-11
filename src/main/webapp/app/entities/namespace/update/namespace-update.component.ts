import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NamespaceFormService, NamespaceFormGroup } from './namespace-form.service';
import { INamespace } from '../namespace.model';
import { NamespaceService } from '../service/namespace.service';

@Component({
  standalone: true,
  selector: 'jhi-namespace-update',
  templateUrl: './namespace-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NamespaceUpdateComponent implements OnInit {
  isSaving = false;
  namespace: INamespace | null = null;

  editForm: NamespaceFormGroup = this.namespaceFormService.createNamespaceFormGroup();

  constructor(
    protected namespaceService: NamespaceService,
    protected namespaceFormService: NamespaceFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ namespace }) => {
      this.namespace = namespace;
      if (namespace) {
        this.updateForm(namespace);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const namespace = this.namespaceFormService.getNamespace(this.editForm);
    if (namespace.id !== null) {
      this.subscribeToSaveResponse(this.namespaceService.update(namespace));
    } else {
      this.subscribeToSaveResponse(this.namespaceService.create(namespace));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INamespace>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(namespace: INamespace): void {
    this.namespace = namespace;
    this.namespaceFormService.resetForm(this.editForm, namespace);
  }
}
